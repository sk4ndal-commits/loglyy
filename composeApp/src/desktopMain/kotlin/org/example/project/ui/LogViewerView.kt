package org.example.project.ui

import ContentView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.ui.footer.FooterView
import org.example.project.ui.header.HeaderView

@Composable
fun LogViewerView(isDarkTheme: MutableState<Boolean>)
{
    // Centralized state for the selected log file path
    val currentLogFilePath = remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            // Pass a callback to HeaderView for updating the log file path
            HeaderView(
                onLoadLogs = { selectedPath ->
                    currentLogFilePath.value = selectedPath
                },
                currentFileName = currentLogFilePath.value?.substringAfterLast(
                    "/"
                ),
                isDarkTheme = isDarkTheme
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Pass the current log file path to ContentView
                ContentView(
                    currentLogFilePath = currentLogFilePath,
                    isDarkTheme = isDarkTheme
                )
            }
        },
        bottomBar = { FooterView() }
    )
}