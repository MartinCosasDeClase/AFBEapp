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
                if (token != null) {
                    val response = retrofitInstance.api.getActos()
                    Log.d("XXX", response.toString())
                    _actos.postValue(response)
                } else {
                    Log.d("XXX", "No se ha encontrado el tocken")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
