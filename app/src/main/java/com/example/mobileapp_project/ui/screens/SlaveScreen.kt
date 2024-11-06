// SlaveScreen.kt
package com.example.mobileapp_project.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobileapp_project.bluetooth.BluetoothHelper

@Composable
fun SlaveScreen(
    bluetoothHelper: BluetoothHelper,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Slave Screen", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Logic for starting data collection
            bluetoothHelper.startDataCollection()
        }) {
            Text("Start Data Collection")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Logic for stopping data collection
            bluetoothHelper.stopDataCollection()
        }) {
            Text("Stop Data Collection")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack) {
            Text("Back")
        }
    }
}
