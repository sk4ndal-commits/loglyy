package org.example.project.views.sidebar

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SavedProfilesListView()
{
    // Placeholder for saved profiles list
    Text(
        "No saved profiles",
        fontSize = 12.sp,
        color = Color.Gray,
        modifier = Modifier.padding(top = 8.dp)
    )
}