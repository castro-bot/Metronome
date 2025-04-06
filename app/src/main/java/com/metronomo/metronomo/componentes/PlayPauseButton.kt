import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.metronomo.metronomo.R  // Ajusta esto según tu paquete

@Composable
fun PlayPauseButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(80.dp)
    ) {
        val iconRes = if (isPlaying) {
            R.drawable.ic_pause // Tu vector XML con icono de pausa
        } else {
            R.drawable.ic_play  // Otro vector XML con icono de play
        }

        Image(
            painter = painterResource(id = iconRes),
            contentDescription = if (isPlaying) "Pausar" else "Reproducir",
            modifier = Modifier.size(48.dp)
        )
    }
}
