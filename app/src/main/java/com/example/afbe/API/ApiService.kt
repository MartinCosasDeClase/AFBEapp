package com.example.afbe.API

import com.example.afbe.API.LoginFeatures.LoginRequest
import com.example.afbe.API.LoginFeatures.LoginResponse
import com.example.afbe.reservas.Reserva
import com.example.afbe.eventos.Acto
import com.example.afbe.partituras.Partitura
import com.example.afbe.publicaciones.Publicacion
import com.example.afbe.user.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("/api/actos")
    suspend fun getActos(): List<Acto>

    @GET("/api/partituras")
    suspend fun getPartituras(): List<Partitura>

    @GET("/api/users/{nif}")
    suspend fun getUserByNif(@Path("nif") nif: String): User

    @GET("/api/partituras/{nif}")
    suspend fun getPartituraByInstrumento(@Path("nif") nif: String): List<Partitura>

    @POST("/api/crear/reserva")
    suspend fun crearReserva(@Body reserva: Reserva): Response<String>

    @GET("/api/disponibilidad")
    suspend fun consultarDisponibilidad(@Query("fecha") fecha: String): Response<List<Long>>

    @POST("/auth/check")
    suspend fun checkToken(@Query("tocken") token: String?): Boolean

    @GET("/api/publicaciones")
    suspend fun getPublicaciones(): List<Publicacion>

    @POST("/api/users/register-token")
    suspend fun registerFcmToken(@Body data: Map<String, String>)

    @POST("/api/actos/{actoId}/asistencia/{usuarioId}/confirmar")
    suspend fun confirmarAsistencia(
        @Path("actoId") actoId: Long,
        @Path("usuarioId") usuarioId: String
    ): Response<String>



}

