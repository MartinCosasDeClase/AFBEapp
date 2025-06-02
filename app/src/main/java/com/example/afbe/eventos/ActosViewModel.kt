package com.example.afbe.eventos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.afbe.MainActivity
import com.example.afbe.preferences.UserPreferences
import kotlinx.coroutines.launch

class ActosViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    private val _actos = MutableLiveData<List<Acto>>()
    val actos: LiveData<List<Acto>> get() = _actos
    val retrofitInstance = MainActivity.retrofitInstance

    fun fetchActos() {
        viewModelScope.launch {
            try {
                val token = userPreferences.getToken()
                val userId = userPreferences.getUserNif()

                if (token != null && userId != null) {
                    val actos = retrofitInstance.api.getActos()
                    _actos.postValue(actos)
                } else {
                    Log.d("XXX", "No se ha encontrado el token o el userId")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun confirmarAsistencia(actoId: Long, usuarioId: String?, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val token = userPreferences.getToken()
                if (token != null) {
                    val response = retrofitInstance.api.confirmarAsistencia(actoId, userPreferences.getUserNif() as String)
                    Log.e("RESPONSECODE", response.code().toString())
                    if (response.isSuccessful) {
                        Log.d("resPONSE",response.message().toString())
                        onSuccess()
                    } else {
                        Log.d("RESPONSEEE",response.message())
                        onError("Error en la confirmación: ${response.code()} ${response.message()}")
                    }
                } else {
                    onError("Token no disponible")
                }
            } catch (e: Exception) {
                onError("Excepción: ${e.message}")
            }
        }
    }

    fun getUserId(): String?{
        var nif = "" as String?
        viewModelScope.launch(){
            nif = userPreferences.getUserNif()
        }
        return nif
    }
    fun clearToken(){
        viewModelScope.launch(){
            userPreferences.clearToken()
        }
    }
}
