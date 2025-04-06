package com.metronomo.metronomo.componentes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties

@Composable
fun BPMDisplay(
    bpm: Int,
    noteType: String,
    onBpmChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showBpmDialog by remember { mutableStateOf(false) }
    var tempBpm by remember { mutableStateOf("") }

    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Símbolo de nota según el tipo
        val noteSymbol = when (noteType) {
            "Blanca" -> "𝅗𝅥"
            "Negra" -> "♩"
            "Corchea" -> "♪"
            "Semicorchea" -> "♬"
            "Tresillo" -> "♪♪♪"
            else -> "♩"
        }
        Text(
            text = "$noteSymbol = $bpm bpm",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.clickable {
                tempBpm = bpm.toString()
                showBpmDialog = true
            }
        )
    }

    if (showBpmDialog) {
        AlertDialog(
            onDismissRequest = {
                showBpmDialog = false
                tempBpm = ""
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            text = {
                OutlinedTextField(
                    value = tempBpm,
                    onValueChange = { newValue ->
                        if (newValue.isEmpty() || (newValue.all { it.isDigit() } && newValue.length <= 3)) {
                            tempBpm = newValue
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            val newBpm = tempBpm.toIntOrNull()
                            if (newBpm != null && newBpm in 20..300) {
                                onBpmChange(newBpm)
                            }
                            showBpmDialog = false
                            tempBpm = ""
                        }
                    ),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    placeholder = { Text("20-300") }
                )
            },
            confirmButton = { }
        )
    }
}