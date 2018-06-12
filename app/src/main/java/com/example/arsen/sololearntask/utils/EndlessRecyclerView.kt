package com.example.arsen.sololearntask.utils

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

abstract class EndlessRecyclerView(context: Context, var mLayoutManager: RecyclerView.LayoutManager) : RecyclerView.OnScrollListener() {

    private var visibleThreshold = 10
    private var previousTotalItemCount = 0
    private var loading = true


    fun EndlessRecyclerView(layoutManager: LinearLayoutManager) {
        this.mLayoutManager = layoutManager
    }

    fun EndlessRecyclerView(layoutManager: GridLayoutManager) {
        this.mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        var lastVisibleItemPosition = 0
        val totalItemCount = mLayoutManager.itemCount

        if (mLayoutManager is LinearLayoutManager) {
            lastVisibleItemPosition = (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()

        } else if (mLayoutManager is GridLayoutManager) {
            lastVisibleItemPosition = (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
        }

        if (totalItemCount < previousTotalItemCount) {
            this.previousTotalItemCount = totalItemCount

            if (totalItemCount == 0) {
                this.loading = true
            }
        }

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false
            previousTotalItemCount = totalItemCount
        }

        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            onLoadMore()
            loading = true
        }

    }

    abstract fun onLoadMore()

}