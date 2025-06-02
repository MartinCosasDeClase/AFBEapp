package com.example.afbe.publicaciones

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.afbe.navController.AppDrawer
import com.example.afbe.navController.TopBar
import com.example.afbe.viewmodels.PublicacionesViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PublicacionesScreen(navController: NavHostController, viewModel: PublicacionesViewModel) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val publicaciones by viewModel.publicaciones.observeAsState(emptyList())

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    LaunchedEffect(Unit) {
        viewModel.fetchPublicaciones()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                navController = navController,
                drawerState = drawerState,
                currentRoute = currentRoute,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("publicaciones") { inclusive = true }
                    }
                    viewModel.clearToken()
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar("Publicaciones", navController) {
                    coroutineScope.launch { drawerState.open() }
                }
            },
            content = { paddingValues ->
                LazyColumn(
                    modifier = Modifier.padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(publicaciones.size) { index ->
                        val publicacion = publicaciones[index]
                        PublicacionCard(publicacion)
                    }
                }
            }
        )
    }
}
