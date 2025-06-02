package com.example.afbe.navController

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@Composable
fun AppDrawer(
    navController: NavHostController,
    drawerState: DrawerState,
    currentRoute: String,
    onLogout: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val drawerItemColors = NavigationDrawerItemDefaults.colors(
        selectedContainerColor = Color(0xFFA8E6CF),
        unselectedIconColor = Color.Gray,
        unselectedTextColor = Color.Black
    )


    ModalDrawerSheet {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Menú", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(24.dp))

            fun navigate(route: String, inclusive: Boolean = false) {
                coroutineScope.launch {
                    drawerState.close()
                    if (inclusive) {
                        navController.navigate(route) {
                            popUpTo(currentRoute) { this.inclusive = true }
                        }
                    } else {
                        navController.navigate(route)
                    }
                }
            }

            NavigationDrawerItem(
                label = { Text("Inicio") },
                selected = currentRoute == "home",
                onClick = { navigate("home") },
                icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                colors = drawerItemColors
            )

            NavigationDrawerItem(
                label = { Text("Partituras") },
                selected = currentRoute == "partituras",
                onClick = { navigate("partituras") },
                icon = { Icon(Icons.Default.LibraryMusic, contentDescription = "Partituras") },
                colors = drawerItemColors
            )

            NavigationDrawerItem(
                label = { Text("Reservas") },
                selected = currentRoute == "reservas",
                onClick = { navigate("reservas") },
                icon = { Icon(Icons.Default.EventAvailable, contentDescription = "Reservas") },
                colors = drawerItemColors
            )

            NavigationDrawerItem(
                label = { Text("Actos") },
                selected = currentRoute == "actos",
                onClick = { navigate("actos") },
                icon = { Icon(Icons.Default.Celebration, contentDescription = "Actos") },
                colors = drawerItemColors
            )

            NavigationDrawerItem(
                label = { Text("Publicaciones") },
                selected = currentRoute == "publicaciones",
                onClick = { navigate("publicaciones") },
                icon = { Icon(Icons.Default.Article, contentDescription = "Publicaciones") },
                colors = drawerItemColors
            )

            NavigationDrawerItem(
                label = { Text("Cerrar sesión", color = Color.Red) },
                selected = false,
                onClick = {
                    coroutineScope.launch {
                        drawerState.close()
                        onLogout()
                    }
                },
                icon = {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión", tint = Color.Red)
                }
            )

        }
    }
}
