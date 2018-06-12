package com.example.arsen.sololearntask.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.MenuItem
import com.example.arsen.sololearntask.R
import com.example.arsen.sololearntask.utils.IMAGE_URL
import com.example.arsen.sololearntask.utils.NEWS_CATEGORY
import com.example.arsen.sololearntask.utils.NEWS_TITLE
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.article_activity.*

class ArticleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.article_activity)
        window.sharedElementEnterTransition =
                TransitionInflater.from(this).inflateTransition(R.transition.shared_element_transition)
        val image = intent.getStringExtra(IMAGE_URL)
        Picasso
                .with(this)
                .load(image)
                .into(articleImage)

        articleCategory.text = intent.getStringExtra(NEWS_CATEGORY)
        articleNews.text = intent.getStringExtra(NEWS_TITLE)
    }


    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {

            R.id.home -> supportFinishAfterTransition()

        }
        return super.onOptionsItemSelected(item)
    }

}

