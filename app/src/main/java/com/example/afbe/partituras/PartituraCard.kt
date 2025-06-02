package com.example.afbe.partituras

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController

@Composable
fun PartituraCard(partitura: Partitura, navController: NavController) {
    val context = LocalContext.current
    val baseUrl = "https://definite-cobra-diverse.ngrok-free.app"
    val imageUrl = "$baseUrl/images/${partitura.imagen}"

    var mostrarImagen by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.MusicNote,
                    contentDescription = "Partitura",
                    tint = Color(0xFF961A1A),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = partitura.obra,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Instrumento: ${partitura.instrumento}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        val encodedUrl = Uri.encode(imageUrl)
                        navController.navigate("verPartitura/$encodedUrl")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA8E6CF))
                ) {
                    Text(text = "Ver", color = Color(0xFF961A1A))
                }
                Button(
                    onClick = {
                        FileDownloader.downloadFile(
                            context = context,
                            url = imageUrl,
                            filename = "${partitura.obra}.jpg"
                        )

                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB3E5FC))
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Descargar",
                        tint = Color(0xFF0D47A1)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Descargar", color = Color(0xFF0D47A1))
                }

            }

        }
    }
}
