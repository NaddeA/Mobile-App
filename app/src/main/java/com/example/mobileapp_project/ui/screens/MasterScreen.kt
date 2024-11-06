// MasterScreen.kt
package com.example.mobileapp_project.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobileapp_project.bluetooth.BluetoothHelper

@Composable
fun MasterScreen(
    bluetoothHelper: BluetoothHelper, // Change viewModel to bluetoothHelper or accept MasterViewModel
    onBack: () -> Unit
) {
    var connectionStatus by remember { mutableStateOf("Disconnected") }
    var commandText by remember { mutableStateOf("") }

    // UI layout code here
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Connection Status: $connectionStatus", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = commandText,
            onValueChange = { commandText = it },
            label = { Text("Enter Command") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                bluetoothHelper.sendCommand(commandText) // Send command through BluetoothHelper
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = connectionStatus == "Connected"
        ) {
            Text("Send Command")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack) {
            Text("Back")
        }
    }
}
