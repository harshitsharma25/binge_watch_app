package com.example.movie_and_tvshows_app.utils

import com.example.movie_and_tvshows_app.network.WatchmodeApiService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient{
    val retrofit : Retrofit =
         Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    fun createApiService(): WatchmodeApiService {
        return retrofit.create(WatchmodeApiService::class.java)
    }
}