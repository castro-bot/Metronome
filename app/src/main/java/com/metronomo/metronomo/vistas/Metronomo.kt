package com.metronomo.metronomo.vistas

import PlayPauseButton
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.metronomo.metronomo.R
import com.metronomo.metronomo.componentes.BPMDisplay
import com.metronomo.metronomo.componentes.MetronomoControls
import com.metronomo.metronomo.componentes.MusicBackground
import com.metronomo.metronomo.componentes.NavigationTextButton
import com.metronomo.metronomo.componentes.SoundSelectorCard
import com.metronomo.metronomo.viewmodels.MetronomoViewModel
import kotlinx.coroutines.*
import kotlin.math.PI
import kotlin.math.sin

enum class SoundType {
    DEFAULT,
    SOUND1,
    SOUND2
}

@Composable
fun Metronomo(navController: NavController, viewModel: MetronomoViewModel) {
    val compasState by viewModel.compas.collectAsState()
    val figuracionState by viewModel.figuracion.collectAsState()

    var bpm by remember { mutableStateOf(60) }
    var isPlaying by remember { mutableStateOf(false) }
    var noteType by remember { mutableStateOf(figuracionState) }
    var selectedCompas by remember { mutableStateOf(compasState) }
    var currentBeat by remember { mutableStateOf(0) }

    // Estado para el sonido seleccionado
    var selectedSound by remember { mutableStateOf(SoundType.DEFAULT) }

    var showDialogCompas by remember { mutableStateOf(false) }
    var showDialogFiguras by remember { mutableStateOf(false) }

    // Variable para almacenar el Job de la coroutine del metrónomo
    var metronomeJob by remember { mutableStateOf<Job?>(null) }

    LaunchedEffect(compasState, figuracionState) {
        noteType = figuracionState
        selectedCompas = compasState
    }

    // Variables Room
    val compas by viewModel.compas.collectAsState()
    val figuracion by viewModel.figuracion.collectAsState()

    fun onSeleccionCompas(nuevoCompas: String) {
        viewModel.actualizarPreferencias(nuevoCompas, figuracion)
        if (isPlaying) {
            metronomeJob?.cancel()
            metronomeJob = null
            isPlaying = false
        }
    }

    fun onSeleccionFiguracion(nuevaFiguracion: String) {
        viewModel.actualizarPreferencias(compas, nuevaFiguracion)
        if (isPlaying) {
            metronomeJob?.cancel()
            metronomeJob = null
            isPlaying = false
        }
    }

    val figuras = listOf(
        "Blanca",
        "Negra",
        "Corchea",
        "Tresillo de corchea",
        "Tresillo de negra",
        "Tresillo de semicorchea",
        "Semicorchea"
    )

    fun getAvailableFiguras(selectedCompas: String): List<String> {
        return when (selectedCompas) {
            "3/8", "6/8", "12/8" -> figuras.filter { it == "Corchea" || it == "Semicorchea" || it == "Tresillo de semicorchea" }
            "2/4", "3/4", "4/4" -> figuras.filter {
                it == "Negra" || it == "Corchea" || it == "Tresillo de corchea" || it == "Semicorchea"
            }
            "2/2" -> listOf("Blanca", "Negra", "Corchea", "Tresillo de negra", "Semicorchea")
            else -> figuras.filter { it != "Blanca" }
        }
    }

    fun generarSonido(isStrongBeat: Boolean, subdivision: Int): ShortArray {
        val sampleRate = 44100
        val frequency = when (selectedSound) {
            SoundType.DEFAULT -> if (isStrongBeat && subdivision == 1) 600.0 else 300.0
            SoundType.SOUND1 -> if (isStrongBeat && subdivision == 1) 800.0 else 400.0
            SoundType.SOUND2 -> if (isStrongBeat && subdivision == 1) 500.0 else 250.0
        }

        val numSamples = (sampleRate * 0.05).toInt() // 50 ms aprox.
        val generatedSound = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val sample = (sin(2 * PI * i / (sampleRate / frequency)) * Short.MAX_VALUE).toInt().toShort()
            generatedSound[i] = sample
        }
        return generatedSound
    }

    suspend fun playMetronome() {
        while (isPlaying) {
            val beatsPerCompas = when (selectedCompas) {
                "2/4" -> 2
                "3/4" -> 3
                "4/4" -> 4
                "3/8" -> 3
                "6/8" -> 6
                "12/8" -> 12
                "2/2" -> 2
                else -> 4
            }

            // Duración de un tiempo (depende del compás)
            val durationPerBeat = when (selectedCompas) {
                "3/8", "6/8", "12/8" -> 60000 / (bpm * 2) // corchea
                "2/2" -> 60000 / bpm  // blanca
                else -> 60000 / bpm  // negra
            }

            // Calcular subdivisiones según el compás y la figuración
            val subdivisions = if (selectedCompas == "2/2") {
                when (noteType) {
                    "Blanca" -> 1
                    "Negra" -> 2
                    "Corchea" -> 4
                    "Tresillo de negra" -> 3
                    "Semicorchea" -> 8
                    else -> 1
                }
            } else {
                when (noteType) {
                    "Corchea" -> if (selectedCompas in listOf("3/8", "6/8", "12/8")) 1 else 2
                    "Semicorchea" -> if (selectedCompas in listOf("3/8", "6/8", "12/8")) 2 else 4
                    "Tresillo de semicorchea" -> 3
                    "Tresillo de corchea" -> 3
                    else -> 1
                }
            }

            for (beat in 1..beatsPerCompas) {
                if (!isPlaying) break
                currentBeat = beat
                val isStrongBeat = beat == 1

                for (subdivisionIndex in 1..subdivisions) {
                    if (!isPlaying) break

                    val sonido = generarSonido(isStrongBeat, subdivisionIndex)
                    val audioTrack = AudioTrack(
                        AudioManager.STREAM_MUSIC,
                        44100,
                        AudioFormat.CHANNEL_OUT_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        sonido.size * 2,
                        AudioTrack.MODE_STATIC
                    )

                    audioTrack.write(sonido, 0, sonido.size)
                    audioTrack.play()

                    val tickDuration = durationPerBeat / subdivisions
                    delay(tickDuration.toLong())
                    audioTrack.release()
                }
            }
        }
    }

    // Lanzamos o cancelamos la reproducción según isPlaying
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            metronomeJob = launch(Dispatchers.Default) { playMetronome() }
        } else {
            metronomeJob?.cancel()
            metronomeJob = null
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MusicBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavigationTextButton(
                    text = "Diapasón",
                    usePrimaryColor = true,
                    onClick = { navController.navigate("Afinador") }
                )
                SoundSelectorCard(
                    selectedSound = selectedSound,
                    onSoundSelected = { newSound -> selectedSound = newSound }
                )
            }

            val beatsPerCompas = when (selectedCompas) {
                "2/4" -> 2
                "3/4" -> 3
                "4/4" -> 4
                "3/8" -> 3
                "6/8" -> 6
                "12/8" -> 12
                "2/2" -> 2
                else -> 4
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                for (beat in 1..beatsPerCompas) {
                    Box(
                        modifier = Modifier
                            .size(25.dp)
                            .padding(4.dp)
                            .background(
                                color = if (beat == currentBeat) {
                                    if (beat == 1) Color.Red else Color(0xFFB71C1C)
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant
                                },
                                shape = CircleShape
                            )
                    )
                }
            }

            BPMDisplay(
                bpm = bpm,
                noteType = noteType,
                onBpmChange = { newBpm -> bpm = newBpm }
            )

            Box(
                modifier = Modifier
                    .size(250.dp)
                    .pointerInput(Unit) {
                        detectVerticalDragGestures { _, dragAmount ->
                            bpm = (bpm - dragAmount.toInt() / 5).coerceIn(20, 300)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val totalDegrees = 360f
                    val stepAngle = totalDegrees / 40
                    val center = size / 2f
                    val radius = size.minDimension / 2.5f

                    drawCircle(
                        color = Color(0xFF2C2C2C),
                        radius = size.minDimension / 2f,
                        style = androidx.compose.ui.graphics.drawscope.Fill
                    )

                    for (i in 0 until 40) {
                        val angle = stepAngle * i
                        val startX = center.width + radius * kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat()
                        val startY = center.height + radius * kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat()
                        val endX = center.width + (radius - 8.dp.toPx()) * kotlin.math.cos(Math.toRadians(angle.toDouble())).toFloat()
                        val endY = center.height + (radius - 8.dp.toPx()) * kotlin.math.sin(Math.toRadians(angle.toDouble())).toFloat()
                        drawLine(
                            color = Color(0xFF444444),
                            start = androidx.compose.ui.geometry.Offset(startX, startY),
                            end = androidx.compose.ui.geometry.Offset(endX, endY),
                            strokeWidth = 2f
                        )
                    }

                    drawCircle(
                        color = Color(0xFF666666),
                        radius = 8.dp.toPx(),
                        center = androidx.compose.ui.geometry.Offset(
                            center.width - radius + 10.dp.toPx(),
                            center.height + radius - 10.dp.toPx()
                        )
                    )

                    drawCircle(
                        color = Color(0xFF1C1C1C),
                        radius = size.minDimension / 3.2f,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4.dp.toPx())
                    )
                }
            }

            MetronomoControls(
                compas = selectedCompas,
                noteType = noteType,
                onCompasClick = { showDialogCompas = true },
                onNoteTypeClick = { showDialogFiguras = true }
            )

            PlayPauseButton(
                isPlaying = isPlaying,
                onClick = { isPlaying = !isPlaying }
            )
        }
    }

    // Diálogo para seleccionar figuración
    if (showDialogFiguras) {
        AlertDialog(
            onDismissRequest = { showDialogFiguras = false },
            title = { Text(text = "Selecciona una Figuración") },
            text = {
                var highlightedFigura by remember { mutableStateOf<String?>(null) }
                val density = LocalDensity.current
                val configuration = LocalConfiguration.current
                val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }
                val availableFiguras = getAvailableFiguras(selectedCompas)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(availableFiguras) { figura ->
                        val isHighlighted = highlightedFigura == figura
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    noteType = figura
                                    showDialogFiguras = false
                                    viewModel.actualizarPreferencias(selectedCompas, figura)
                                }
                                .padding(8.dp)
                                .background(
                                    color = if (isHighlighted) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                    else Color.Transparent,
                                    shape = MaterialTheme.shapes.large
                                )
                                .onGloballyPositioned { layoutCoordinates ->
                                    val itemCenter = layoutCoordinates.positionInWindow().y + layoutCoordinates.size.height / 2
                                    if (itemCenter in (screenHeightPx / 2 - 100)..(screenHeightPx / 2 + 100)) {
                                        highlightedFigura = figura
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            val imageResId = when (figura) {
                                "Blanca" -> R.drawable.blanca
                                "Negra" -> R.drawable.negra
                                "Corchea" -> R.drawable.corchea
                                "Tresillo de corchea" -> R.drawable.tresillo
                                "Tresillo de negra" -> R.drawable.tresillonegra1
                                "Tresillo de semicorchea" -> R.drawable.tresillosemicorchea
                                "Semicorchea" -> R.drawable.semicorchea
                                else -> R.drawable.negra
                            }
                            Image(
                                painter = painterResource(id = imageResId),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }

    // Diálogo para seleccionar compás
    if (showDialogCompas) {
        AlertDialog(
            onDismissRequest = { showDialogCompas = false },
            title = { Text(text = "Selecciona un Compás") },
            text = {
                var highlightedCompas by remember { mutableStateOf<String?>(null) }
                val density = LocalDensity.current
                val configuration = LocalConfiguration.current
                val screenHeightPx = with(density) { configuration.screenHeightDp.dp.toPx() }
                val compases = listOf("2/4", "3/4", "4/4", "3/8", "6/8", "12/8", "2/2")
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(compases) { compas ->
                        val isHighlighted = highlightedCompas == compas
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedCompas = compas
                                    showDialogCompas = false
                                    viewModel.actualizarPreferencias(compas, noteType)
                                }
                                .padding(8.dp)
                                .background(
                                    color = if (isHighlighted) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                    else Color.Transparent,
                                    shape = MaterialTheme.shapes.large
                                )
                                .onGloballyPositioned { layoutCoordinates ->
                                    val itemCenter = layoutCoordinates.positionInWindow().y + layoutCoordinates.size.height / 2
                                    if (itemCenter in (screenHeightPx / 2 - 100)..(screenHeightPx / 2 + 100)) {
                                        highlightedCompas = compas
                                    }
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = compas,
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (isHighlighted) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }
}
