package com.example.afbe.eventos

import android.annotation.SuppressLint
import android.util.Log
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
fun ActosScreen(navController: NavHostController, viewModel: ActosViewModel) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val actos by viewModel.actos.observeAsState(emptyList())

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.substringBefore("/") ?: ""


    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.fetchActos()
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
                        popUpTo("actos") { inclusive = true }
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar("Actos", navController) {
                    coroutineScope.launch { drawerState.open() }
                }
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            content = { paddingValues ->
                LazyColumn(modifier = Modifier.padding(paddingValues)) {
                    items(actos.size) { index ->
                        val acto = actos[index]
                        ActoCard(acto) { actoId ->
                            coroutineScope.launch {
                                val usuarioId = viewModel.getUserId()

                                viewModel.confirmarAsistencia(
                                    actoId = actoId,
                                    usuarioId = usuarioId,
                                    onSuccess = {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Asistencia confirmada")
                                        }
                                    },
                                    onError = { errorMsg ->
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Asistencia confirmada")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}