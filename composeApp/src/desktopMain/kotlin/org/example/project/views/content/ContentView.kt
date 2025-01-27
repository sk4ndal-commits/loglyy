import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import org.example.project.data.LogRow
import org.example.project.providers.ReaderProvider
import org.example.project.views.sidebar.SidebarView
import org.example.project.views.tabs.TabsView

@Composable
fun ContentView(
    currentLogFilePath: MutableState<String?>,
    isDarkTheme: MutableState<Boolean>,
    readerProvider: ReaderProvider
)
{
    // Sidebar and Tabs weights
    var sidebarWeight by remember { mutableStateOf(0.25f) }
    var message by remember { mutableStateOf(LogRow()) }
    val minWeight = 0.1f
    val maxWeight = 0.9f

    // Provide the log file path via CompositionLocalProvider
    CompositionLocalProvider(LocalLogFilePath provides currentLogFilePath) {
        Row(Modifier.fillMaxSize()) {
            // SidebarView that reacts to the current log file path
            SidebarView(
                modifier = Modifier.weight(sidebarWeight),
                logRow = message
            )

            // Draggable divider for resizing the Sidebar and Tabs
            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(8.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val delta = dragAmount.x / 1000f
                            sidebarWeight = (sidebarWeight + delta).coerceIn(
                                minWeight,
                                maxWeight
                            )
                        }
                    }
                    .background(Color.LightGray)
            )

            // TabsView that reacts to the current log file path
            TabsView(
                modifier = Modifier.weight(1f - sidebarWeight),
                onRowClicked = { message = it },
                isDarkTheme = isDarkTheme,
                readerProvider = readerProvider
            )
        }
    }
}