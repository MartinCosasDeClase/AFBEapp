package com.example.afbe.homePage

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
import com.example.afbe.eventos.ActoCard
import com.example.afbe.eventos.ActosViewModel
import com.example.afbe.navController.AppDrawer
import com.example.afbe.navController.TopBar
import com.example.afbe.publicaciones.PublicacionCard
import com.example.afbe.viewmodels.PublicacionesViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavHostController,
    actosViewModel: ActosViewModel,
    publicacionesViewModel: PublicacionesViewModel
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val actos by actosViewModel.actos.observeAsState(emptyList())
    val publicaciones by publicacionesViewModel.publicaciones.observeAsState(emptyList())

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    LaunchedEffect(Unit) {
        actosViewModel.fetchActos()
        publicacionesViewModel.fetchPublicaciones()
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
                        popUpTo("home") { inclusive = true }
                    }
                    actosViewModel.clearToken()
                    publicacionesViewModel.clearToken()
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar("Inicio", navController) {
                    coroutineScope.launch { drawerState.open() }
                }
            },
            content = { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item {
                        Text(
                            text = "Último acto",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        actos.lastOrNull()?.let { ultimoActo ->
                            ActoCard(
                                acto = ultimoActo,
                                onConfirmarAsistencia = { actoId ->
                                    coroutineScope.launch {
                                        val usuarioId = actosViewModel.getUserId()
                                        actosViewModel.confirmarAsistencia(
                                            actoId = actoId,
                                            usuarioId = usuarioId,
                                            onSuccess = {
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar("Asistencia confirmada")
                                                }
                                            },
                                            onError = { errorMsg ->
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar("Error: $errorMsg")
                                                }
                                            }

                                        )
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Asistencia confirmada")
                                        }

                                    }
                                }
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    item {
                        Text(
                            text = "Últimas publicaciones",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    val ultimasPublicaciones = publicaciones.takeLast(3).reversed()

                    items(ultimasPublicaciones.size) { index ->
                        val publicacion = ultimasPublicaciones[index]
                        PublicacionCard(publicacion)
                    }
                }

            }
        )
    }
}
