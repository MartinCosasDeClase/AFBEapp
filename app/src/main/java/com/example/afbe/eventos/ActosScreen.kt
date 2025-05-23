package com.example.afbe.eventos

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.afbe.navController.TopBar
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ActosScreen(navController: NavHostController, viewModel: ActosViewModel) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val actos by viewModel.actos.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchActos()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Menú", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(24.dp))

                    NavigationDrawerItem(
                        label = { Text("Inicio") },
                        selected = false,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navController.navigate("home")
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") }
                    )

                    NavigationDrawerItem(
                        label = { Text("Partituras") },
                        selected = false,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navController.navigate("partituras")
                        },
                        icon = { Icon(Icons.Default.Favorite, contentDescription = "Partituras") }
                    )

                    NavigationDrawerItem(
                        label = { Text("Ensayos") },
                        selected = false,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navController.navigate("ensayos")
                        },
                        icon = { Icon(Icons.Default.Favorite, contentDescription = "Ensayos") }
                    )

                    NavigationDrawerItem(
                        label = { Text("Actos") },
                        selected = true,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                        },
                        icon = { Icon(Icons.Default.Favorite, contentDescription = "Eventos") }
                    )

                    NavigationDrawerItem(
                        label = { Text("Cerrar sesión", color = Color.Red) },
                        selected = false,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navController.navigate("login") {
                                popUpTo("eventos") { inclusive = true }
                            }
                        },
                        icon = {
                            Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión", tint = Color.Red)
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopBar("Actos", navController) {
                    coroutineScope.launch { drawerState.open() }
                }
            },
            content = { paddingValues ->
                LazyColumn(modifier = Modifier.padding(paddingValues)) {
                    Log.d("XXXScreen", actos.toString())
                    items(actos.size) { index ->
                        val acto = actos[index]
                        ActoCard(acto)
                    }
                }
            }
        )
    }
}
