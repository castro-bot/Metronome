package com.metronomo.metronomo.navegacion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.metronomo.vistas.Afinador

import com.metronomo.metronomo.viewmodels.AfinadorViewModel
import com.metronomo.metronomo.viewmodels.MetronomoViewModel
import com.metronomo.metronomo.vistas.Inicio
import com.metronomo.metronomo.vistas.Metronomo
import com.metronomo.metronomo.vistas.UltimasPreferencias

@Composable
fun Navegacion(afinadorViewModel: AfinadorViewModel, metronomoViewModel: MetronomoViewModel) {
    val navController = rememberNavController()
    val mostUsedPreference by metronomoViewModel.mostUsedPreference.collectAsState()
    val isLoading by metronomoViewModel.isLoading.collectAsState()

    // Esperamos a que la carga inicial termine antes de decidir la navegación
    if (!isLoading) {
        NavHost(
            navController = navController,
            startDestination = if (metronomoViewModel.hayPreferenciasGuardadas())
                "UltimasPreferencias" else "Inicio"
        ) {
            composable("Inicio") {
                Inicio(navController)
            }
            composable("UltimasPreferencias") {
                UltimasPreferencias(navController, metronomoViewModel)
            }
            composable("Metronomo") {
                Metronomo(navController, metronomoViewModel)
            }
            composable("Afinador") {
                Afinador(navController)
            }
        }
    } else {
        // Mostrar pantalla de carga mientras se inicializan las preferencias
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}