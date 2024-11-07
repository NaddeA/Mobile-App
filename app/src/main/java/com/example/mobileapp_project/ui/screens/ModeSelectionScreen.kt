package com.example.mobileapp_project.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobileapp_project.Mode

@Composable
fun ModeSelectionScreen(onModeSelected: (Mode) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { onModeSelected(Mode.Master) }) {
            Text("Master Mode")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onModeSelected(Mode.Slave) }) {
            Text("Slave Mode")
        }
    }
}
