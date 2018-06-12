package com.example.arsen.sololearntask.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.arsen.sololearntask.R
import com.example.arsen.sololearntask.model.NewsResult
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.news_feed_linear_item.view.*


class NewsAdapter(private val context: Context) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private var news: ArrayList<NewsResult> = ArrayList()
    var mListener: NewsAdapterListener? = null
    var isButtonClicked: Boolean = false


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsInstance = news[position]
        holder.newsCategory.text = newsInstance.category
        holder.newsTitle.text = newsInstance.fields!!.title
        if (newsInstance.fields!!.image.isNotEmpty()) {
            Picasso.with(context).load(newsInstance.fields!!.image).into(holder.newsImage)
        }
        holder.itemView.setOnClickListener {
            mListener?.onItemClick(newsInstance, holder)
        }

        holder.pinImageButton.setOnClickListener {
            if (mListener != null) {
                mListener?.onItemPinClick(newsInstance,holder)
            }
        }
        if (newsInstance.isPinned){
            val fillPin: Drawable = context.getDrawable(R.drawable.full_pin)
            holder.pinImageButton.setImageDrawable(fillPin)
        }
    }

    override fun getItemCount(): Int {
        return news.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == 1) {
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.news_feed_grid_item, parent, false))
        } else {
            ViewHolder(LayoutInflater.from(context).inflate(R.layout.news_feed_linear_item, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isButtonClicked) 1 else 0
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val newsTitle = view.newsTitle!!
        val newsCategory = view.newsCategory!!
        val newsImage = view.newsImage!!
        val pinImageButton = view.pinNewsButton!!
    }

    fun updateData(newsData: List<NewsResult>) {
        news.addAll(newsData)
        notifyItemRangeInserted(news.size - newsData.size, news.size - 1)
    }

    interface NewsAdapterListener {
        fun onItemPinClick(news: NewsResult,holder: ViewHolder)

        fun onItemClick(news: NewsResult, holder: ViewHolder)

    }

}