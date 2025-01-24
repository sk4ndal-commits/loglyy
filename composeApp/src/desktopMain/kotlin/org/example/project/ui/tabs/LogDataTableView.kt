import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.example.project.readers.TextLogFileReader

@Composable
fun LogDataTableView(
    onRowClick: (List<String>) -> Unit,
    isDarkTheme: MutableState<Boolean>,
    logFileReader: ILogFileReader = TextLogFileReader(),
    batchSize: Int = 100
)
{
    val filePath = LocalLogFilePath.current.value ?: return
    var displayedRows by remember { mutableStateOf<List<List<String>>>(emptyList()) }
    var loadingMore by remember { mutableStateOf(false) }
    var endOfFile by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    var selectedRowIndex by remember { mutableStateOf(-1) }

    DisposableEffect(filePath) {
        val job = scope.launch {
            logFileReader.process(filePath, batchSize).collect { batch ->
                if (batch.isEmpty())
                {
                    endOfFile = true
                } else
                {
                    displayedRows = displayedRows + batch
                    loadingMore = false
                }
            }
        }
        onDispose { job.cancel() }
    }

    Column(Modifier.padding(16.dp)) {
        // Table header
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf(
                "Timestamp",
                "Level",
                "Source",
                "Message",
                "Detail Message"
            ).forEach { header ->
                Text(
                    text = header,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Divider(Modifier.fillMaxWidth(), thickness = 2.dp, color = Color.Gray)

        // Scrollable LazyColumn to display rows of data
        LazyColumn(Modifier.fillMaxSize()) {
            itemsIndexed(displayedRows) { index, row ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            selectedRowIndex = index
                            onRowClick(row)
                        }
                        .background(
                            GetSelectedLineColor(
                                selectedRowIndex,
                                index,
                                isDarkTheme
                            ),
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    row.forEach { cell ->
                        Text(
                            text = cell,
                            fontSize = 12.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Trigger loading next batch lazily when scrolling reaches the bottom
                if (index == displayedRows.lastIndex && !loadingMore && !endOfFile)
                {
                    loadingMore = true
                    scope.launch {
                        logFileReader.process(filePath, batchSize)
                            .collect { batch ->
                                if (batch.isEmpty())
                                {
                                    endOfFile = true
                                } else
                                {
                                    displayedRows = displayedRows + batch
                                    loadingMore = false
                                }
                            }
                    }
                }
            }

            // "Loading more" indicator when incrementally loading rows
            if (loadingMore)
            {
                item {
                    Text(
                        text = "Loading more...",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        fontSize = 14.sp
                    )
                }
            }

            // End of file indicator when all data is loaded
            if (endOfFile && displayedRows.isNotEmpty())
            {
                item {
                    Text(
                        text = "End of file reached",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

fun GetSelectedLineColor(
    selectedRowIndex: Int,
    index: Int,
    isDarkTheme: MutableState<Boolean>
): Color
{

    if (selectedRowIndex == index)
    {
        return if (isDarkTheme.value)
        {
            Color.DarkGray
        } else
        {
            Color.LightGray
        }
    }

    return Color.Transparent
}
