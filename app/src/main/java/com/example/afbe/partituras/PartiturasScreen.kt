package com.example.afbe.partituras

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.afbe.navController.AppDrawer
import com.example.afbe.navController.TopBar
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PartiturasScreen(navController: NavHostController, viewModel: PartiturasViewModel) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val partituras by viewModel.partituras.observeAsState(emptyList())

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    LaunchedEffect(Unit) {
        viewModel.fetchPartituras()
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
                        popUpTo("partituras") { inclusive = true }
                    }
                    viewModel.clearToken()
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar("Partituras", navController) {
                    coroutineScope.launch { drawerState.open() }
                }
            },
            content = { paddingValues ->
                LazyColumn(modifier = Modifier.padding(paddingValues)) {
                    items(partituras.size) { index ->
                        val partitura = partituras[index]
                        PartituraCard(partitura, navController)
                    }
                }
            }
        )
    }
}
