package com.metronomo.metronomo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.metronomo.metronomo.viewmodels.AfinadorViewModel
import com.metronomo.metronomo.viewmodels.MetronomoViewModel
import com.metronomo.metronomo.navegacion.Navegacion
import com.metronomo.metronomo.ui.theme.MetronomoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val afinadorViewModel: AfinadorViewModel by viewModels()
        val metronomoViewModel: MetronomoViewModel by viewModels {
            MetronomoViewModel.Factory(application)
        }
        setContent {
            MetronomoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navegacion(
                        afinadorViewModel = afinadorViewModel,
                        metronomoViewModel = metronomoViewModel
                    )
                }
            }
        }
    }
}

