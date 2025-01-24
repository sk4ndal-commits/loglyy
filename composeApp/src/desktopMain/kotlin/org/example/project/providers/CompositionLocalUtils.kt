import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf

// CompositionLocal to hold the current log file path
val LocalLogFilePath = compositionLocalOf { mutableStateOf<String?>(null) }