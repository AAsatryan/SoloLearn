package com.example.arsen.sololearntask.api

import com.example.arsen.sololearntask.utils.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {

    private var retrofit : Retrofit? = null

    fun getNews():Retrofit{
        if(retrofit == null){
            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        return retrofit!!
    }
}