package com.example.afbe.reservas

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.afbe.MainActivity
import com.example.afbe.preferences.UserPreferences
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class ReservaViewModel (private val userPreferences: UserPreferences) : ViewModel() {

    private val apiService = MainActivity.retrofitInstance.api

    fun crearReserva(
        sala: String,
        nif: String,
        fecha: LocalDate,
        horaInicio: LocalTime,
        horaFin: LocalTime,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val salaId = when (sala) {
                    "Sala 1" -> 1L
                    "Sala d’assaig" -> 2L
                    "Sala 2" -> 3L
                    else -> 0L
                }
                val reserva = Reserva(
                    salaId = salaId,
                    nif = userPreferences.getUserNif() as String,
                    fecha = fecha.toString(),
                    horaInicio = horaInicio.toString(),
                    horaFin = horaFin.toString()
                )
                Log.e("RESERVA", reserva.toString())
                val response = apiService.crearReserva(reserva)
                onResult(response.isSuccessful)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }

//    fun consultarDisponibilidad(
//        fecha: LocalDate,
//        callback: (List<String>) -> Unit
//    ) {
//        viewModelScope.launch {
//            try {
//                val response = apiService.consultarDisponibilidad(fecha.toString())
//                if (response.isSuccessful) {
//                    val ids = response.body() ?: emptyList()
//                    val salas = ids.mapNotNull {
//                        when (it) {
//                            1L -> "Sala 1"
//                            2L -> "Sala d’assaig"
//                            3L -> "Sala 2"
//                            else -> null
//                        }
//                    }
//                    callback(salas)
//                } else {
//                    callback(emptyList())
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                callback(emptyList())
//            }
//        }
//    }
    fun clearToken(){
        viewModelScope.launch(){
            userPreferences.clearToken()
        }
    }
}