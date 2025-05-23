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

    private val _partitura = MutableLiveData<List<Partitura>>()
    val partituras: LiveData<List<Partitura>> get() = _partitura
    val retrofitInstance = MainActivity.retrofitInstance

    fun fetchPartituras() {
        viewModelScope.launch {
            try {
                val token = userPreferences.getToken()
                if (token != null) {
                    val response = retrofitInstance.api.getPartituras()
                    Log.d("XXX", response.toString())
                    _partitura.postValue(response)
                } else {
                    Log.d("XXX", "No se ha encontrado el tocken")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
