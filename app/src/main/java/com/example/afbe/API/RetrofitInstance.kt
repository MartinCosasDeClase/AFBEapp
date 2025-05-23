package com.example.afbe.API

import android.annotation.SuppressLint
import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.afbe.preferences.UserPreferences

class RetrofitInstance(private val context: Context) {

    private val BASE_URL = "https://definite-cobra-diverse.ngrok-free.app"


    private val userPreferences: UserPreferences = UserPreferences(context)

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(userPreferences))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val api: ApiService = retrofit.create(ApiService::class.java)
}
