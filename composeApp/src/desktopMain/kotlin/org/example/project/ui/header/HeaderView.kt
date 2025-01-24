package org.example.project.ui.header

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.awt.FileDialog

@Composable
fun HeaderView(
    onLoadLogs: (String) -> Unit,
    currentFileName: String?,
    isDarkTheme: MutableState<Boolean>
)
{
    TopAppBar(
        title = {

        },
        actions = {
            Button(onClick = {
                val filePath = openFileDialog()
                if (filePath != null)
                {
                    onLoadLogs(filePath)
                }
            }) { Text("Load Logs") }
            Spacer(modifier = Modifier.width(8.dp))

            ThemeSwitch(isDarkTheme)
        },
        elevation = 4.dp
    )
}

@Composable
fun ThemeSwitch(isDarkTheme: MutableState<Boolean>)
{
    Switch(
        checked = isDarkTheme.value,
        onCheckedChange = { isDarkTheme.value = it },
        colors = androidx.compose.material.SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            uncheckedThumbColor = Color.Black,
            checkedTrackColor = Color.White,
            uncheckedTrackColor = Color.Black,
        )
    )
}


fun openFileDialog(): String?
{
    val fileDialog =
        FileDialog(ComposeWindow(), "Choose a file", FileDialog.LOAD)
    fileDialog.isVisible = true
    return fileDialog.file?.let {
        "${fileDialog.directory}$it"
    }
}
