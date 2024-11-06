package com.example.mobileapp_project.ui.screens

// BluetoothConnectionScreen.kt

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobileapp_project.bluetooth.BluetoothHelper

@SuppressLint("MissingPermission")
@Composable
fun BluetoothConnectionScreen(
    bluetoothHelper: BluetoothHelper,
    onDeviceConnected: () -> Unit,
    onBack: () -> Unit
) {
    // This would be updated with a device selection mechanism in a real app
    val exampleDevice: BluetoothDevice? = bluetoothHelper.getPairedDevices().firstOrNull()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bluetooth Connection", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            exampleDevice?.let { device ->
                if (bluetoothHelper.connectToDevice(device) { connectedDevice, socket ->
                        if (socket != null) {
                            onDeviceConnected()
                        } else {
                            println("Failed to connect to device: ${connectedDevice.name}")
                        }
                    }) {
                    println("Attempting to connect...")
                } else {
                    println("Connection failed or permissions are missing.")
                }
            } ?: println("No device available to connect.")
        }) {
            Text("Connect to Device")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack) {
            Text("Back")
        }
    }
}

