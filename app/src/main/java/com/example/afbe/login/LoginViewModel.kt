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
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LoginViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    private val retrofitInstance = MainActivity.retrofitInstance

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

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getFcmToken(): String = suspendCoroutine { cont ->
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    cont.resume(task.result!!)
                } else {
                    cont.resumeWithException(task.exception ?: Exception("No se pudo obtener token FCM"))
                }
            }
    }


    fun onLoginSelected(onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = LoginRequest(email.value ?: "", password.value ?: "")
                val response = retrofitInstance.api.login(request)

                if (response.isSuccessful && response.body() != null) {
                    val token = response.body()!!.token
                    val nif = response.body()!!.nif

                    userPreferences.saveToken(token)
                    userPreferences.saveUserNif(nif)

                    // Aquí esperas el token FCM de forma suspendida
                    val fcmToken = try {
                        getFcmToken()
                    } catch (e: Exception) {
                        Log.e("LoginViewModel", "Error obteniendo token FCM", e)
                        ""
                    }

                    if (fcmToken.isNotEmpty()) {
                        try {
                            retrofitInstance.api.registerFcmToken(
                                mapOf("nif" to nif, "token" to fcmToken)
                            )
                            Log.d("LoginViewModel", "Token FCM enviado al backend")
                        } catch (e: Exception) {
                            Log.e("LoginViewModel", "Error enviando token FCM tras login", e)
                        }
                    }

                    onSuccess()
                } else {
                    onError()
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadUserNif() {
        viewModelScope.launch {
            val storedNif = userPreferences.getUserNif() ?: ""
            _nif.value = storedNif
        }
    }

    private fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    private fun isValidPassword(password: String) = password.isNotEmpty()
}
