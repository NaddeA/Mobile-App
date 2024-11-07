package com.example.mobileapp_project.ui.theme

import androidx.compose.foundation.layout.*
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
    onStartAcceptingConnections: () -> Unit,
    discoveredDevices: List<String>,
    onConnectToDevice: (String) -> Unit
) {
    // UI Layout: Use a Column for stacking all the items vertically.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Button to Toggle Bluetooth On/Off
        Button(
            onClick = onBluetoothToggle,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Toggle Bluetooth")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to Discover Devices Nearby
        Button(
            onClick = onDiscoverDevices,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Discover Devices")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to Get List of Paired Devices
        Button(
            onClick = onGetPairedDevices,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Get Paired Devices")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to Make Device Discoverable
        Button(
            onClick = onDiscoverability,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Make Discoverable")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to Start Accepting Connections
        Button(
            onClick = onStartAcceptingConnections,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Accepting Connections")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display the Discovered Devices
        Text("Discovered Devices:", modifier = Modifier.align(Alignment.Start))

        Spacer(modifier = Modifier.height(8.dp))

        // Display each discovered device in a list with a connect button
        if (discoveredDevices.isNotEmpty()) {
            discoveredDevices.forEach { device ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(device, modifier = Modifier.weight(1f))
                    Button(onClick = { onConnectToDevice(device) }) {
                        Text("Connect")
                    }
                }
            }
        } else {
            // If no devices found, display a message
            Text("No devices found", modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}
