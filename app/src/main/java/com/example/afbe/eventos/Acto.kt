package com.example.afbe.eventos


data class Acto(
    val id: Long,
    val nombre: String,
    val ubicacion: String,
    val fechaHora: String,
    val descripcion: String,
    val partitura: String,
    val asistencia: Boolean
)
