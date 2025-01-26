package org.example.project.views.sidebar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DatePickerView(label: String)
{
    Text(
        label,
        fontSize = 12.sp,
        color = Color.Gray,
        modifier = Modifier.padding(bottom = 4.dp)
    )
    TextField(
        value = "",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth()
    )
}