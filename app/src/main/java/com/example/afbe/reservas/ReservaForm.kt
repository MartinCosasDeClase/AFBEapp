package com.example.afbe.reservas

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaForm(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    selectedSala: String,
    horaEntrada: LocalTime,
    horaSalida: LocalTime,
    salasDisponibles: List<String>,
    mensajeReserva: String,
    onDateChange: (LocalDate) -> Unit,
    onSalaChange: (String) -> Unit,
    onHoraEntradaChange: (LocalTime) -> Unit,
    onHoraSalidaChange: (LocalTime) -> Unit,
    onConfirmReserva: () -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.getDefault())
    val initialMillis = selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)
    var showDatePicker by remember { mutableStateOf(false) }

    val colorVerdeAgua = Color(0xFFA8E6CF)
    val colorGranate = Color(0xFF961A1A)

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val localDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        onDateChange(localDate)
                    }
                    showDatePicker = false
                }) {
                    Text("Aceptar", color = colorGranate)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar", color = colorGranate)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = colorGranate,
                    todayDateBorderColor = colorGranate
                )
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Reserva tu sala",
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFF2C2C2C)
        )

        OutlinedCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Fecha seleccionada", style = MaterialTheme.typography.titleSmall, color = Color.Gray)
                Text(
                    text = selectedDate.format(dateFormatter),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF222222)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = { showDatePicker = true },
                        colors = ButtonDefaults.buttonColors(containerColor = colorGranate)
                    ) {
                        Text("Seleccionar fecha", color = Color.White)
                    }
                }
            }
        }

        OutlinedCard {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Selecciona una sala", style = MaterialTheme.typography.titleSmall, color = Color.Gray)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Sala 1", "Sala d’assaig", "Sala 2").forEach { sala ->
                        Button(
                            onClick = { onSalaChange(sala) },
                            shape = MaterialTheme.shapes.medium,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedSala == sala) colorVerdeAgua else Color.LightGray,
                                contentColor = if (selectedSala == sala) Color.White else Color.Black
                            )
                        ) {
                            Text(sala)
                        }
                    }
                }
            }
        }

        OutlinedCard {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HoraPicker("Hora de entrada", horaEntrada, onHoraEntradaChange, colorVerdeAgua)
                HoraPicker("Hora de salida", horaSalida, onHoraSalidaChange, colorVerdeAgua)
            }
        }

        Button(
            onClick = onConfirmReserva,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(containerColor = colorGranate)
        ) {
            Text("CONFIRMAR RESERVA", color = Color.White)
        }

        if (mensajeReserva.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = mensajeReserva,
                color = if (mensajeReserva.contains("éxito", ignoreCase = true)) Color.Green else Color.Red
            )
        }
    }
}


@Composable
fun HoraPicker(
    label: String,
    hora: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    buttonColor: Color
) {
    val context = LocalContext.current
    val horaStr = hora.format(DateTimeFormatter.ofPattern("HH:mm"))

    val showTimePicker = remember { mutableStateOf(false) }

    if (showTimePicker.value) {
        TimePickerDialog(
            context,
            { _, hour: Int, minute: Int ->
                onTimeSelected(LocalTime.of(hour, minute))
                showTimePicker.value = false
            },
            hora.hour,
            hora.minute,
            true
        ).show()
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(label, style = MaterialTheme.typography.titleSmall, color = Color.Gray)
        Button(
            onClick = { showTimePicker.value = true },
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
        ) {
            Text(horaStr, color = Color.White)
        }
    }
}

