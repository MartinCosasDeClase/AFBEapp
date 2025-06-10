package com.example.afbe.API

import android.annotation.SuppressLint
import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.afbe.preferences.UserPreferences
import com.example.afbe.utils.LocalDateAdapter
import com.example.afbe.utils.LocalTimeAdapter
import com.google.gson.GsonBuilder
import java.time.LocalDate
import java.time.LocalTime

class RetrofitInstance(private val context: Context) {

    private val BASE_URL = "https://afbespringboot-production.up.railway.app"
    //private val BASE_URL = "https://definite-cobra-diverse.ngrok-free.app"
    private val userPreferences: UserPreferences = UserPreferences(context)

    val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .registerTypeAdapter(LocalTime::class.java, LocalTimeAdapter())
        .create()

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(userPreferences))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    val api: ApiService = retrofit.create(ApiService::class.java)
}
