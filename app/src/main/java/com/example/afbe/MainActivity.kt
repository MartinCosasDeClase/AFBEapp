package com.example.afbe

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }

        FirebaseMessaging.getInstance().subscribeToTopic("all")
            .addOnCompleteListener { task ->
                val msg = if (task.isSuccessful) "Suscrito al topic" else "Error al suscribirse"
                Log.d("FCM", msg)
            }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM", "Token: $token")
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
