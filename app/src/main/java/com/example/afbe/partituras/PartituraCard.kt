package com.example.afbe.partituras


import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.afbe.eventos.Acto

@Composable
fun PartituraCard(partitura: Partitura) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Obra: ${partitura.obra}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Instrumento ID: ${partitura.instrumento}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

