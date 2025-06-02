package com.example.afbe.navController

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.afbe.MainActivity
import com.example.afbe.R
import com.example.afbe.preferences.UserPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    navController: NavHostController,
    onMenuClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    var userImageUrl by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val api = MainActivity.retrofitInstance.api
    val baseUrl = "https://definite-cobra-diverse.ngrok-free.app"

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val response = api.getUserByNif(userPreferences.getUserNif() as String)
                userImageUrl = baseUrl+"/images/"+response.userImage
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate("Perfil") }) {
                if (userImageUrl.isNullOrEmpty()) {
                    Image(
                        painter = painterResource(id = R.drawable.afbelogo),
                        contentDescription = "Imagen de perfil",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(36.dp)
                    )
                } else {
                    AsyncImage(
                        model = userImageUrl,
                        contentDescription = "Imagen de perfil",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(36.dp),
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.afbelogo),
                        placeholder = painterResource(id = R.drawable.afbelogo)
                    )
                }
            }
        }
    )
}
