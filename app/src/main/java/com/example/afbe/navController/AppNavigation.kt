package com.example.afbe.navController

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.afbe.ensayos.EnsayosScreen
import com.example.afbe.eventos.ActosViewModel
import com.example.afbe.eventos.ActosScreen
import com.example.afbe.homePage.HomeScreen
import com.example.afbe.login.LoginScreen
import com.example.afbe.login.LoginViewModel
import com.example.afbe.partituras.PartiturasScreen
import com.example.afbe.partituras.PartiturasViewModel
import com.example.afbe.preferences.UserPreferences
import com.example.afbe.user.UserScreen
import com.example.afbe.user.UserViewModel


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val loginViewModel = remember { LoginViewModel(userPreferences) }
    val actoViewModel = remember { ActosViewModel(userPreferences) }
    val partituraViewModel = remember { PartiturasViewModel(userPreferences) }
    val userViewModel = remember { UserViewModel(userPreferences) }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(viewModel = loginViewModel) {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }

        composable("home") {
            HomeScreen(navController)
        }

        composable("partituras") {
            PartiturasScreen(
                navController,
                partituraViewModel
            )
        }

        composable("ensayos") {
            EnsayosScreen()
        }

        composable("Actos") {
            ActosScreen(navController, actoViewModel)
        }
        composable("Perfil") {
            UserScreen(onBackClick = { navController.popBackStack() }, userViewModel)
        }

    }
}
