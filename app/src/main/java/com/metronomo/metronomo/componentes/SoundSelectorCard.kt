package com.metronomo.metronomo.componentes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.metronomo.metronomo.vistas.SoundType


// Componente que muestra el ícono y la carta para la selección de sonido
@Composable
fun SoundSelectorCard(
    selectedSound: SoundType,
    onSoundSelected: (SoundType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Ajustes del sonido",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        if (expanded) {
            Card(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .align(Alignment.TopEnd),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Sonido del metrónomo", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    SoundOptionRow(
                        text = "Predeterminado",
                        isSelected = selectedSound == SoundType.DEFAULT
                    ) {
                        onSoundSelected(SoundType.DEFAULT)
                        expanded = false
                    }

                    SoundOptionRow(
                        text = "Sonido 1",
                        isSelected = selectedSound == SoundType.SOUND1
                    ) {
                        onSoundSelected(SoundType.SOUND1)
                        expanded = false
                    }

                    SoundOptionRow(
                        text = "Sonido 2",
                        isSelected = selectedSound == SoundType.SOUND2
                    ) {
                        onSoundSelected(SoundType.SOUND2)
                        expanded = false
                    }
                }
            }
        }
    }
}

@Composable
fun SoundOptionRow(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = isSelected, onClick = onClick)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}
