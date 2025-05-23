package com.example.afbe.homePage

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.afbe.navController.TopBar
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Menú", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(24.dp))

                    NavigationDrawerItem(
                        label = { Text("Inicio") },
                        selected = true,
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
                        selected = false,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navController.navigate("actos")
                        },
                        icon = { Icon(Icons.Default.Favorite, contentDescription = "Eventos") }
                    )

                    NavigationDrawerItem(
                        label = { Text("Cerrar sesión", color = Color.Red) },
                        selected = false,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navController.navigate("login") {
                                popUpTo("home") { inclusive = true }
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
                TopBar("Inicio", navController) {
                    coroutineScope.launch { drawerState.open() }
                }
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    repeat(10) { index ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Publicación ${index + 1}", style = MaterialTheme.typography.titleMedium)
                                Text("Contenido de la publicación ${index + 1}", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        )
    }
}
