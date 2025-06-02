package com.example.afbe.login

import android.annotation.SuppressLint
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.afbe.API.LoginFeatures.LoginRequest
import com.example.afbe.FireBase.MyFirebaseMessagingService
import com.example.afbe.MainActivity
import com.example.afbe.preferences.UserPreferences
import kotlinx.coroutines.launch

class LoginViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    private val retrofitInstance = MainActivity.retrofitInstance
    @SuppressLint("StaticFieldLeak")
    private val myFirebaseMessagingService = MyFirebaseMessagingService()

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnabled = MutableLiveData<Boolean>()
    val loginEnabled: LiveData<Boolean> = _loginEnabled

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _nif = MutableLiveData<String>()
    val nif: LiveData<String> = _nif

    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _loginEnabled.value = isValidEmail(email) && isValidPassword(password)
    }

    fun onLoginSelected(onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            val request = LoginRequest(email.value ?: "", password.value ?: "")
            val response = retrofitInstance.api.login(request)

            if (response.isSuccessful && response.body() != null) {
                val token = response.body()!!.token
                val nif = response.body()!!.nif
                userPreferences.saveToken(token)
                userPreferences.saveUserNif(nif)

                myFirebaseMessagingService.onNewToken(token)

                onSuccess()
            } else {
                onError()
            }
            _isLoading.value = false
        }
    }

    fun loadUserNif() {
        viewModelScope.launch {
            val storedNif = userPreferences.getUserNif() ?: ""
            _nif.value = storedNif
        }
    }

    private fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    private fun isValidPassword(password: String): Boolean = password.length > 0
}
