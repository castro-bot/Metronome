package com.example.metronomo.vistas

import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.metronomo.metronomo.R
import com.metronomo.metronomo.componentes.MusicBackground
import com.metronomo.metronomo.componentes.NavigationTextButton

@Composable
fun Afinador(navController: NavController) {
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var selectedNote by remember { mutableStateOf<String?>(null) }

    fun playNote(note: String, noteResId: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(navController.context, noteResId)
        mediaPlayer?.start()
        selectedNote = note
    }

    val noteImages = mapOf(
        "Do" to R.drawable.dopentagrama2,
        "Re" to R.drawable.repentagrama2,
        "Mi" to R.drawable.mipentagrama2,
        "Fa" to R.drawable.fapentagrama2,
        "Sol" to R.drawable.solpentagrama2,
        "La" to R.drawable.lapentagrama2,
        "Si" to R.drawable.sipentagrama2
    )

    Box(modifier = Modifier.fillMaxSize()) {
        MusicBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            NavigationTextButton(
                text = "Metrónomo",
                usePrimaryColor = true,
                onClick = { navController.navigate("Metronomo") },
            )


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val imageRes = noteImages[selectedNote] ?: R.drawable.pentagrama2png
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = "Pentagrama con nota resaltada",
                        modifier = Modifier.size(370.dp)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                val notes = listOf(
                    "Do" to R.raw.doo,
                    "Re" to R.raw.re,
                    "Mi" to R.raw.mi,
                    "Fa" to R.raw.fa,
                    "Sol" to R.raw.sol,
                    "La" to R.raw.la,
                    "Si" to R.raw.si
                )


                for (row in notes.chunked(3)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        row.forEach { (note, resId) ->
                            FilledTonalButton(
                                onClick = { playNote(note, resId) },
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = if (selectedNote == note)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.secondaryContainer
                                ),
                                elevation = ButtonDefaults.filledTonalButtonElevation(
                                    defaultElevation = 4.dp
                                )
                            ) {
                                Text(
                                    text = note,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = if (selectedNote == note)
                                        MaterialTheme.colorScheme.onPrimary
                                    else
                                        MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}