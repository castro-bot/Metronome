package com.metronomo.metronomo.vistas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.metronomo.metronomo.R
import kotlinx.coroutines.delay

@Composable
fun Inicio(navController: NavController) {
    // Navegación automática después de 3 segundos
    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate("Metronomo") {
            popUpTo("Inicio") { inclusive = true } // Elimina la pantalla principal de la pila de navegación
        }
    }
    // UI de la pantalla principal
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo1),
            contentDescription = "",
            modifier = Modifier.size(400.dp)
        )
        Text(
            text = "YourMetronome",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
        )
    }
}
