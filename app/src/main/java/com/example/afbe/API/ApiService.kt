package com.example.afbe.API

import com.example.afbe.API.LoginFeatures.LoginRequest
import com.example.afbe.API.LoginFeatures.LoginResponse
import com.example.afbe.eventos.Acto
import com.example.afbe.partituras.Partitura
import com.example.afbe.user.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("/api/actos")
    suspend fun getActos(): List<Acto>

    @GET("/api/partituras")
    suspend fun getPartituras(): List<Partitura>

    @GET("me")
    suspend fun getMe(
        @Header("Authorization") token: String
    ): User


}
