import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.project.data.LogRow
import org.example.project.viewmodels.LogDataTableViewModel


@Composable
fun LogDataTableView(
    onRowClick: (LogRow) -> Unit,
    isDarkTheme: MutableState<Boolean>,
    viewModel: LogDataTableViewModel = viewModel()
)
{
    // Observe state from the ViewModel
    val rows by viewModel.rows.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val endOfFile by viewModel.endOfFile.collectAsState()

    val filePath = LocalLogFilePath.current.value ?: return
    var selectedRowIndex by remember { mutableStateOf(-1) }

    // Set file path to the ViewModel
    LaunchedEffect(filePath) {
        viewModel.setFilePath(filePath)
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
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(rows) { index, row ->
                Row(
                    // Existing row rendering logic
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            selectedRowIndex = index
                            onRowClick(row)
                        }
                        .background(
                            getSelectedLineColor(
                                selectedRowIndex,
                                index,
                                isDarkTheme
                            ),
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf(
                        row.timeStamp?.toString() ?: "N/A",
                        row.level?.name ?: "N/A",
                        row.source ?: "N/A",
                        row.message ?: "N/A",
                        row.detailMessage ?: "N/A"
                    ).forEach { cell ->
                        Text(
                            text = cell,
                            fontSize = 12.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Loading & EOF indicators remain unchanged
            if (isLoading) {
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
            if (endOfFile && rows.isNotEmpty()) {
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

fun getSelectedLineColor(selectedRowIndex: Int, currentIndex: Int, isDarkTheme: MutableState<Boolean>): Color
{
    return if (selectedRowIndex == currentIndex)
    {
        if (isDarkTheme.value) Color.DarkGray else Color.LightGray
    } else
    {
        Color.Transparent
    }
}
