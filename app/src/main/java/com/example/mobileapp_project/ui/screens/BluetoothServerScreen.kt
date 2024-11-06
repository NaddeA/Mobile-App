package com.example.mobileapp_project.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobileapp_project.bluetooth.BluetoothServerViewModel

@Composable
fun BluetoothServerScreen(viewModel: BluetoothServerViewModel = viewModel()) {
    val connectionStatus by viewModel.connectionStatus.collectAsState()
    var commandText by remember { mutableStateOf("") }

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
                viewModel.sendResponse(commandText) // Send command through ViewModel
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = connectionStatus == "Connected"
        ) {
            Text("Send Command")
        }
    }
}
