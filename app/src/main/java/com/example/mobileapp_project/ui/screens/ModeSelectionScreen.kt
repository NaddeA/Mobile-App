package com.example.mobileapp_project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.example.mobileapp_project.Mode

@Composable
fun ModeSelectionScreen(onModeSelected: (Mode) -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { onModeSelected(Mode.Master) }) {
            Text("Master Mode")
        }
        Button(onClick = { onModeSelected(Mode.Slave) }) {
            Text("Slave Mode")
        }
    }
}
