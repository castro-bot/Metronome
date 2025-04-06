package com.metronomo.metronomo.vistas

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.metronomo.metronomo.R
import com.metronomo.metronomo.componentes.MusicBackground
import com.metronomo.metronomo.viewmodels.MetronomoViewModel

@Composable
fun UltimasPreferencias(
    navController: NavController,
    metronomoViewModel: MetronomoViewModel
) {
    val ultimaPreferencia by metronomoViewModel.mostUsedPreference.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        MusicBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¡Bienvenido de nuevo!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            ultimaPreferencia?.let { preferencia ->
                Text(
                    text = "Tu configuración más usada:",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Mostrar el compás
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Compás",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = preferencia.compas,
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                // Mostrar la figuración
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Figuración",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            val imageResId = when (preferencia.figuracion) {
                                "Blanca" -> R.drawable.blanca
                                "Negra" -> R.drawable.negra
                                "Corchea" -> R.drawable.corchea
                                "Tresillo" -> R.drawable.tresillo
                                "Semicorchea" -> R.drawable.semicorchea
                                else -> R.drawable.negra
                            }
                            Image(
                                painter = painterResource(id = imageResId),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = preferencia.figuracion,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            if (preferencia.usosTotales > 1) {
                                Text(
                                    text = "(Usado ${preferencia.usosTotales} veces)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        metronomoViewModel.cargarPreferenciasGuardadas()
                        navController.navigate("Metronomo") {
                            popUpTo("UltimasPreferencias") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Continuar con esta configuración")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navController.navigate("Metronomo") {
                            popUpTo("UltimasPreferencias") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Usar otra configuración")
                }
            }
        }
    }
}