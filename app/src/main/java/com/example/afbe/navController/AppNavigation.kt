package com.example.afbe.navController

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.afbe.MainActivity
import com.example.afbe.reservas.ReservaScreen
import com.example.afbe.reservas.ReservaViewModel
import com.example.afbe.eventos.ActosScreen
import com.example.afbe.eventos.ActosViewModel
import com.example.afbe.homePage.HomeScreen
import com.example.afbe.login.LoginScreen
import com.example.afbe.login.LoginViewModel
import com.example.afbe.partituras.PartituraViewerScreen
import com.example.afbe.partituras.PartiturasScreen
import com.example.afbe.partituras.PartiturasViewModel
import com.example.afbe.preferences.UserPreferences
import com.example.afbe.publicaciones.PublicacionesScreen
import com.example.afbe.user.UserScreen
import com.example.afbe.user.UserViewModel
import com.example.afbe.utils.ClaveSolLoader
import com.example.afbe.viewmodels.PublicacionesViewModel
import java.net.URLDecoder

@Composable
fun AppNavigation() {
    val apiService = MainActivity.retrofitInstance.api
    val navController = rememberNavController()
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val loginViewModel = remember { LoginViewModel(userPreferences) }
    val actoViewModel = remember { ActosViewModel(userPreferences) }
    val partituraViewModel = remember { PartiturasViewModel(userPreferences) }
    val userViewModel = remember { UserViewModel(userPreferences) }
    val reservaViewModel = remember { ReservaViewModel(userPreferences) }
    val publicacionesViewModel = remember { PublicacionesViewModel(userPreferences) }

    val startDestination by produceState<String?>(initialValue = null) {
        val token = userPreferences.getToken()
        if(!token.isNullOrEmpty()) {
            value = if (!apiService.checkToken(token)) "login" else "home"
        }else{
            value = "login"
        }
    }

    if (startDestination == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ClaveSolLoader()
        }
        return
    }

    NavHost(navController = navController, startDestination = startDestination!!) {
        composable("login") {
            LoginScreen(viewModel = loginViewModel) {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }

        composable("home") {
            HomeScreen(navController, actoViewModel, publicacionesViewModel)
        }

        composable("partituras") {
            PartiturasScreen(navController, partituraViewModel)
        }

        composable("reservas") {
            ReservaScreen(navController, reservaViewModel)
        }

        composable("actos") {
            ActosScreen(navController, actoViewModel)
        }
        composable("publicaciones") {
            PublicacionesScreen(navController, publicacionesViewModel)
        }

        composable("perfil") {
            UserScreen(
                onBackClick = { navController.popBackStack() },
                viewModel = userViewModel
            )
        }
        composable(
            "verPartitura/{url}",
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { backStackEntry ->
            val urlEncoded = backStackEntry.arguments?.getString("url") ?: ""
            val url = URLDecoder.decode(urlEncoded, "UTF-8")

            PartituraViewerScreen(
                imageUrl = url,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

