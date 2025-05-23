package com.example.afbe.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.afbe.MainActivity
import com.example.afbe.preferences.UserPreferences
import kotlinx.coroutines.launch

class UserViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user
    private val retrofitInstance = MainActivity.retrofitInstance

    fun fetchUser() {
        viewModelScope.launch {
            try {
                val token = userPreferences.getToken()
                if (token != null) {
                    Log.d("TOKEN", token)
                    val response = retrofitInstance.api.getMe("Bearer $token")
                    _user.postValue(response)
                    Log.d("USER", "Usuario recibido: $response")
                } else {
                    Log.d("USER", "No se ha encontrado el token")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("USER", "Error al obtener el usuario", e)
            }
        }
    }
}
