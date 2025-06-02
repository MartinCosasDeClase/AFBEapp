package com.example.afbe.partituras

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.afbe.MainActivity
import com.example.afbe.preferences.UserPreferences
import kotlinx.coroutines.launch

class PartiturasViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    private val _partituras = MutableLiveData<List<Partitura>>()
    val partituras: LiveData<List<Partitura>> get() = _partituras
    private val retrofitInstance = MainActivity.retrofitInstance

    fun fetchPartituras() {
        viewModelScope.launch {
            try {
                val userNif = userPreferences.getUserNif()
                if (!userNif.isNullOrEmpty()) {
                    Log.d("FETCH_PARTITURAS", "Obteniendo partituras para el NIF: $userNif")
                    val response = retrofitInstance.api.getPartituraByInstrumento(userNif)
                    _partituras.postValue(response)
                    Log.d("FETCH_PARTITURAS", "Partituras recibidas: ${response.size}")
                } else {
                    Log.e("FETCH_PARTITURAS", "No se encontró NIF en DataStore")
                }
            } catch (e: Exception) {
                Log.e("FETCH_PARTITURAS", "Error al obtener partituras desde el backend", e)
            }
        }
    }
    fun clearToken(){
        viewModelScope.launch(){
            userPreferences.clearToken()
        }
    }
}
