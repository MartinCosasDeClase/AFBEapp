package com.example.afbe

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import com.example.afbe.API.RetrofitInstance
import com.example.afbe.navController.AppNavigation
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : ComponentActivity() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var retrofitInstance: RetrofitInstance
            private set
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        retrofitInstance = RetrofitInstance(applicationContext)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
            }
        }


        setContent {
            MaterialTheme {
                AppNavigation()
            }
        }
    }
    object RetrofitClient {
        val retrofitInstance: RetrofitInstance by lazy {
            retrofitInstance
        }
    }


}
