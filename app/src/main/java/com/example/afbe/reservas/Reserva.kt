package com.example.afbe.reservas

data class Reserva(
    val salaId: Long,
    val nif: String,
    val fecha: String,
    val horaInicio: String,
    val horaFin: String
)