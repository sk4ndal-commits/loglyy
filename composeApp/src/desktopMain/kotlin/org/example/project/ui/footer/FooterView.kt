package org.example.project.ui.footer

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FooterView()
{
    BottomAppBar(elevation = 4.dp) {
        Text("Status: Ready", modifier = Modifier.padding(16.dp))
    }
}