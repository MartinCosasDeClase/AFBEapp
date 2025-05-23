package com.example.afbe

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.afbe.API.RetrofitInstance
import com.example.afbe.homePage.HomeScreen
import com.example.afbe.login.LoginScreen
import com.example.afbe.login.LoginViewModel
import com.example.afbe.navController.AppNavigation
import com.example.afbe.ui.theme.AFBETheme

class MainActivity : ComponentActivity() {
    companion  object {
        @SuppressLint("StaticFieldLeak")
        lateinit var retrofitInstance: RetrofitInstance
            private set
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        retrofitInstance = RetrofitInstance(applicationContext)
        setContent {
            MaterialTheme{
                AppNavigation()
            }
        }
    }
}

