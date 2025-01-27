package org.example.project.views.sidebar

import SearchBarView
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.project.data.LogRow

@Composable
fun SidebarView(modifier: Modifier = Modifier, logRow: LogRow)
{
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        // Searchbar
        SearchBarView(
            modifier = Modifier,
            placeholderText = "Search logs...",
            onSearchQueryChanged = { query ->
                // Handle the search query here
            }
        )

        // Date Range Filters
        Text(
            "Date Range",
            fontSize = 14.sp,
            color = MaterialTheme.colors.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        DatePickerView("Start Date")
        DatePickerView("End Date")

        Spacer(modifier = Modifier.height(16.dp))

        // Log Level Filter
        Text(
            "Log Level",
            fontSize = 14.sp,
            color = MaterialTheme.colors.primary
        )
        var selectedLevel by remember { mutableStateOf("All") }
        DropdownMenuButton(
            options = listOf("All", "INFO", "WARN", "ERROR", "DEBUG"),
            selectedOption = selectedLevel,
            onOptionChanged = { selectedLevel = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Apply Filter Button
        Button(
            onClick = { /* TODO: Handle filter logic */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Apply Filters")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Saved Profiles Placeholder
        Text(
            "Saved Profiles",
            fontSize = 14.sp,
            color = MaterialTheme.colors.primary
        )
        SavedProfilesListView()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Log Message Details",
            fontSize = 14.sp,
            color = MaterialTheme.colors.primary
        )
        LogMessageDetailView(
            message = logRow.detailMessage ?: "No details available"
        )
    }
}