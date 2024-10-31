package com.example.mobileapp_project

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

@Composable
fun MainScreen(
    onBluetoothToggle: () -> Unit,
    onDiscoverDevices: () -> Unit,
    onGetPairedDevices: () -> Unit,
    onDiscoverability: () -> Unit,
    discoveredDevices: List<String>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onBluetoothToggle) {
            Text("Toggle Bluetooth")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onDiscoverDevices) {
            Text("Discover Devices")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onGetPairedDevices) {
            Text("Get Paired Devices")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onDiscoverability) {
            Text("Make Discoverable")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Discovered Devices:")
        discoveredDevices.forEach { device ->
            Text(device)
        }
    }
}
