package com.example.afbe.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.afbe.MainActivity.Companion.retrofitInstance
import com.example.afbe.preferences.UserPreferences
import kotlinx.coroutines.launch

class UserViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    fun fetchUser() {
        viewModelScope.launch {
            try {
                val userNif = userPreferences.getUserNif()
                if (!userNif.isNullOrEmpty()) {
                    val response = retrofitInstance.api.getUserByNif(userNif)
                    _user.postValue(response)
                    Log.d("USER", "Usuario obtenido del backend: $response")
                } else {
                    Log.d("USER", "No se encontró NIF en DataStore")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("USER", "Error al obtener el usuario desde el backend", e)
            }
        }
    }

}
