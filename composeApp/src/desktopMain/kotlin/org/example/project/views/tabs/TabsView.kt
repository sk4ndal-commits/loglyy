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

@Composable
fun TabsView(
    modifier: Modifier = Modifier,
    onRowClicked: (List<String>) -> Unit,
    isDarkTheme: MutableState<Boolean>
)
{
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Log Viewer", "Visualizations", "AI Insights")

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
            0 -> LogDataTableView(onRowClicked, isDarkTheme)
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