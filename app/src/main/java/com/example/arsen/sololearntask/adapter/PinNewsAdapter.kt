package com.example.arsen.sololearntask.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.arsen.sololearntask.R
import com.example.arsen.sololearntask.model.NewsResult
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.pined_news_item.view.*


class PinNewsAdapter(private val context: Context) : RecyclerView.Adapter<PinNewsAdapter.ViewHolder>() {

    private var newsRealm: ArrayList<NewsResult> = ArrayList()
    var pListener: PinNewsAdapter.PinNewsAdapterListener?=null


    override fun getItemCount(): Int {
        return newsRealm.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = newsRealm[position]
        holder.pinNewsCategory.text = article.category
        Picasso.with(context).load(article.fields!!.image).into(holder.pinNewsImage)

        holder.itemView.setOnClickListener {
            pListener?.onPinItemClick(article,holder)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.pined_news_item, parent, false))
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val pinNewsImage = view.pinedNewsImage!!
        val pinNewsCategory = view.pinNewsCategory!!

    }

    fun updateData(news: NewsResult) {
        newsRealm.add(news)
        notifyDataSetChanged()
    }

    fun getDataFromRealm(news: List<NewsResult>) {
        newsRealm.addAll(news)
        notifyDataSetChanged()

    }

    interface PinNewsAdapterListener {
        fun onPinItemClick(news: NewsResult, holder: ViewHolder)
    }
}