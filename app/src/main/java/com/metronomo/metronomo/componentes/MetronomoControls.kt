package com.metronomo.metronomo.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MetronomoControls(
    compas: String,
    noteType: String,
    onCompasClick: () -> Unit,
    onNoteTypeClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón de compás
        SquareButton(
            text = compas,
            onClick = onCompasClick,
            textStyle = TextStyle(
                fontSize = 24.sp,       // Tamaño que prefieras
                fontWeight = FontWeight.Bold
            )
        )

        // Botón de figuración
        SquareButton(
            text = when (noteType) {
                "Blanca" -> "𝅗𝅥"
                "Negra" -> "♩"
                "Corchea" -> "♫"
                "Semicorchea" -> "♬"
                "Tresillo de corchea" -> "♪♪♪"
                "Tresillo de negra" -> "♩♩♩"
                "Tresillo de semicorchea" -> "♬♬♬"
                else -> "♩"
            },
            onClick = onNoteTypeClick,
            textStyle = TextStyle(
                fontSize = 32.sp,        // Más grande
                fontWeight = FontWeight.Bold
            )
        )
    }
}