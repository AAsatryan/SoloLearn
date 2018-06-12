package com.example.arsen.sololearntask.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("search?&show-fields=trailText,headline,thumbnail")
    fun getNews(@Query("page")page:Int, @Query("api-key") key: String): Call<ResponseBody>
}