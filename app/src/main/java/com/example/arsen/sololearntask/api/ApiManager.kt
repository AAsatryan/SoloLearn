package com.example.arsen.sololearntask.api

import com.example.arsen.sololearntask.model.NewsResult
import com.example.arsen.sololearntask.utils.KEY
import com.example.arsen.sololearntask.utils.TEST_KEY
import com.example.arsen.sololearntask.utils.await
import com.example.arsen.sololearntask.utils.getDataList


object ApiManager {

    private var apiService: ApiService = RetrofitClient.getNews().create(ApiService::class.java)

    suspend fun getNews(pageNumber:Int): List<NewsResult> {
        val response = apiService.getNews(pageNumber, KEY)
        val await = response.await()
        return await.getDataList(Array<NewsResult>::class.java) as List<NewsResult>
    }
}