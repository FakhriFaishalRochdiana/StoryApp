package com.zaniva.storyappv2.connection

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Connection {
    private const val BASE = "https://story-api.dicoding.dev/v1/"

    val loggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val apiIns = retrofit.create(Query::class.java)
}