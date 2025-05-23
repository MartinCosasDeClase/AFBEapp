package com.example.afbe.API

import okhttp3.Interceptor
import okhttp3.Response
import com.example.afbe.preferences.UserPreferences
import kotlinx.coroutines.runBlocking

class AuthInterceptor(private val userPreferences: UserPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking{userPreferences.getToken()}
        val requestBuilder = chain.request().newBuilder()
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}
