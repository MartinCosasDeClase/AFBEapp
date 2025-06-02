package com.example.afbe.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.afbe.MainActivity
import com.example.afbe.preferences.UserPreferences
import com.example.afbe.publicaciones.Publicacion
import kotlinx.coroutines.launch

class PublicacionesViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    private val _publicaciones = MutableLiveData<List<Publicacion>>()
    val publicaciones: LiveData<List<Publicacion>> get() = _publicaciones

    private val retrofitInstance = MainActivity.retrofitInstance

    fun fetchPublicaciones() {
        viewModelScope.launch {
            try {
                val userNif = userPreferences.getUserNif()
                if (!userNif.isNullOrEmpty()) {
                    Log.d("FETCH_PUBLICACIONES", "Obteniendo publicaciones para el NIF: $userNif")
                    val response = retrofitInstance.api.getPublicaciones()
                    _publicaciones.postValue(response)
                    Log.d("FETCH_PUBLICACIONES", "Publicaciones recibidas: ${response.size}")
                } else {
                    Log.e("FETCH_PUBLICACIONES", "No se encontró NIF en DataStore")
                }
            } catch (e: Exception) {
                Log.e("FETCH_PUBLICACIONES", "Error al obtener publicaciones desde el backend", e)
            }
        }
    }

    fun clearToken() {
        viewModelScope.launch {
            userPreferences.clearToken()
        }
    }
}
