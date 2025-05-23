package com.example.afbe.navController

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.afbe.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String,navController: NavHostController, onMenuClick: () -> Unit = {}) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate("Perfil")}) {
                Image(
                    painter = painterResource(id = R.drawable.afbelogo),
                    contentDescription = "Imagen de perfil",
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(36.dp)
                )
            }
        }
    )
}
