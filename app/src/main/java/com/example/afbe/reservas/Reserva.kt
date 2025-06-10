package com.example.afbe.reservas

import java.time.LocalDate
import java.util.Date

data class Reserva(
    val salaId: Long,
    val nif: String,
    val fecha: LocalDate,
    val horaInicio: String,
    val horaFin: String
)