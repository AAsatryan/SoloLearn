package com.example.arsen.sololearntask.activity

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.example.arsen.sololearntask.R
import com.example.arsen.sololearntask.adapter.NewsAdapter
import com.example.arsen.sololearntask.adapter.PinNewsAdapter
import com.example.arsen.sololearntask.api.ApiManager
import com.example.arsen.sololearntask.job.AlarmReceiver
import com.example.arsen.sololearntask.model.NewsResult
import com.example.arsen.sololearntask.utils.*
import io.realm.Realm
import kotlinx.android.synthetic.main.news_feed_activity.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import java.util.*

class MainActivity : AppCompatActivity(), NewsAdapter.NewsAdapterListener,
        PinNewsAdapter.PinNewsAdapterListener {

    private var newsResult = Realm.getDefaultInstance().where(NewsResult::class.java).findAll()
    private var pinNewsAdapter = PinNewsAdapter(this)
    private var newsAdapter = NewsAdapter(this)
    private var newsJob: Job = Job()
    private var isClicked = false
    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.news_feed_activity)
        getNews()
        newsFeedRecyclerView()
        pinNewsRecyclerView()
        getNotification()
        changeView()
    }

    private fun newsFeedRecyclerView() {
        newsFeed.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        newsFeed.isNestedScrollingEnabled = false
        newsFeed.adapter = newsAdapter
        newsFeed.setHasFixedSize(true)
        newsFeed.setItemViewCacheSize(10)
        newsAdapter.mListener = this
        Realm.getDefaultInstance().executeTransaction {
            newsResult = it.where(NewsResult::class.java).findAll()
            newsAdapter.updateData(newsResult)
        }
        newsFeed.addOnScrollListener(object : EndlessRecyclerView(this, newsFeed.layoutManager) {
            override fun onLoadMore() {
                currentPage++
                getNews()
                if (currentPage >= 5) {
                    return
                }
            }
        })
    }

    private fun pinNewsRecyclerView() {
        pinedNewsFeed.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        pinedNewsFeed.isNestedScrollingEnabled = false
        pinedNewsFeed.adapter = pinNewsAdapter
        pinNewsAdapter.pListener = this
        Realm.getDefaultInstance().executeTransaction {
            val pinNews = it.where(NewsResult::class.java).equalTo("isPinned", true).findAll()
            pinNewsAdapter.getDataFromRealm(pinNews)
        }

    }

    @SuppressLint("ShortAlarm")
    private fun getNotification() {
        val alarmIntent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, alarmIntent, 0)

        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        manager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().timeInMillis,
                1000, pendingIntent)

    }

    private fun getNews() {
        newsJob = async(UI) {
            try {
                progressBar.visibility = View.VISIBLE
                val response = ApiManager.getNews(currentPage)
                updateUI(response)
                Realm.getDefaultInstance().executeTransactionAsync(
                        {
                            it.copyToRealmOrUpdate(response)
                        },
                        {

                        },
                        {
                            Log.e("Error", "Realm", it)
                        }
                )
            } catch (e: Exception) {

                updateUI(newsResult)
            }
        }
    }

    private fun updateUI(response: List<NewsResult>) {
        progressBar.visibility = View.INVISIBLE
        newsAdapter.updateData(response)

    }

    private fun changeView() {
        changeView.setOnClickListener {
            isClicked = !isClicked
            newsAdapter.isButtonClicked = isClicked
            if (isClicked) {
                newsFeed.layoutManager = GridLayoutManager(this, 2)
            } else {
                newsFeed.layoutManager = LinearLayoutManager(this)

            }

        }
    }

    override fun onItemPinClick(news: NewsResult, holder: NewsAdapter.ViewHolder) {
        Realm.getDefaultInstance().executeTransaction {
            news.isPinned = true
            it.copyToRealmOrUpdate(news)
            pinNewsAdapter.updateData(news)
        }
        val fillPin: Drawable = resources.getDrawable(R.drawable.full_pin)
        holder.pinImageButton.setImageDrawable(fillPin)
    }

    override fun onItemClick(news: NewsResult, holder: NewsAdapter.ViewHolder) {
        val intent = Intent(this, ArticleActivity::class.java)
        intent.putExtra(NEWS_CATEGORY, news.category)
        intent.putExtra(IMAGE_URL, news.fields!!.image)
        intent.putExtra(NEWS_TITLE, news.fields!!.title)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, holder.newsImage, "transitionImage")
        startActivity(intent, options.toBundle())
    }

    override fun onPinItemClick(news: NewsResult, holder: PinNewsAdapter.ViewHolder) {
        val intent = Intent(this, ArticleActivity::class.java)
        intent.putExtra(NEWS_CATEGORY, news.category)
        intent.putExtra(IMAGE_URL, news.fields!!.image)
        intent.putExtra(NEWS_TITLE, news.fields!!.title)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, holder.pinNewsImage, "transitionImage")
        startActivity(intent, options.toBundle())

    }
}




