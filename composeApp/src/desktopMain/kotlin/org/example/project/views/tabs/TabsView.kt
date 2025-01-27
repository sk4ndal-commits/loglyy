package org.example.project.views.tabs

import LogDataTableView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.project.data.LogRow
import org.example.project.providers.ReaderProvider
import org.example.project.viewmodels.LogDataTableViewModel
import org.example.project.viewmodels.LogDataTableViewModelFactory

@Composable
fun TabsView(
    modifier: Modifier = Modifier,
    onRowClicked: (LogRow) -> Unit,
    isDarkTheme: MutableState<Boolean>,
    readerProvider: ReaderProvider
)
{
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Log Viewer", "Visualizations", "AI Insights")

    val factory = LogDataTableViewModelFactory(readerProvider = readerProvider)
    val logDataTableViewModel: LogDataTableViewModel = viewModel(factory = factory)

    Column(modifier = modifier) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    modifier = modifier,
                    selected = (index == selectedTab),
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab)
        {
            0 -> LogDataTableView(onRowClicked, isDarkTheme, logDataTableViewModel)
            1 -> PlaceholderTab("Charts & Visualizations (Placeholder)")
            2 -> PlaceholderTab("AI Insights (Patterns, Anomalies)")
        }
    }
}

@Composable
fun PlaceholderTab(text: String)
{
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text)
    }
}