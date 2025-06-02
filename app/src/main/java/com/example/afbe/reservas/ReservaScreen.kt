package com.example.afbe.reservas

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.afbe.navController.AppDrawer
import com.example.afbe.navController.TopBar
import com.example.afbe.preferences.UserPreferences
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReservaScreen(navController: NavHostController, reservaViewModel: ReservaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedSala by remember { mutableStateOf("") }
    var horaEntrada by remember { mutableStateOf(LocalTime.of(9, 0)) }
    var horaSalida by remember { mutableStateOf(LocalTime.of(10, 0)) }

    var salasDisponibles by remember { mutableStateOf(emptyList<String>()) }
    var disponibilidadConsultada by remember { mutableStateOf(false) }
    var mensajeReserva by remember { mutableStateOf("") }

    var nif by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        nif = userPreferences.getUserNif() ?: ""
    }

//    fun consultarDisponibilidad(fecha: LocalDate) {
//        reservaViewModel.consultarDisponibilidad(fecha) { ocupadas ->
//            val todas = listOf("Sala 1", "Sala d’assaig", "Sala 2")
//            val disponibles = todas.filterNot { it in ocupadas }
//            salasDisponibles = disponibles
//            disponibilidadConsultada = true
//        }
//    }

    fun confirmarReserva() {
        if (selectedSala.isEmpty()) {
            mensajeReserva = "Selecciona una sala primero."
            return
        }

        reservaViewModel.crearReserva(
            sala = selectedSala,
            nif = nif,
            fecha = selectedDate,
            horaInicio = horaEntrada,
            horaFin = horaSalida
        ) { exito ->
            mensajeReserva = if (exito) {
                "Reserva realizada con éxito."
            } else {
                "Error al realizar la reserva."
            }
        }
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
                        popUpTo("reservas") { inclusive = true }
                    }
                    reservaViewModel.clearToken()
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopBar("Reservas", navController) {
                    coroutineScope.launch { drawerState.open() }
                }
            },
            content = { paddingValues ->
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)) {

                    ReservaForm(
                        selectedDate = selectedDate,
                        selectedSala = selectedSala,
                        horaEntrada = horaEntrada,
                        horaSalida = horaSalida,
                        salasDisponibles = salasDisponibles,
                        //disponibilidadConsultada = disponibilidadConsultada,
                        mensajeReserva = mensajeReserva,
                        onDateChange = { selectedDate = it },
                        onSalaChange = { selectedSala = it },
                        onHoraEntradaChange = { horaEntrada = it },
                        onHoraSalidaChange = { horaSalida = it },
                        //onConsultarDisponibilidad = { fecha -> consultarDisponibilidad(fecha) },
                        onConfirmReserva = { confirmarReserva() }
                    )
                }
            }
        )
    }
}
