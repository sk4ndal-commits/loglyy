import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import org.example.project.providers.ReaderProvider
import org.example.project.views.LogViewerView

@Composable
fun App()
{
    val isDarkTheme = remember { mutableStateOf(false) }

    val readerProvider = ReaderProvider()

    MaterialTheme(
        colors = if (isDarkTheme.value) darkColors(primary = Color.LightGray) else lightColors(
            primary = Color.DarkGray
        )
    ) {
        LogViewerView(isDarkTheme, readerProvider)
    }
}