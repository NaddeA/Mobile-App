package com.example.mobileapp_project.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobileapp_project.channels.ChannelsViewModel
import com.example.mobileapp_project.bluetooth.BluetoothHelper
import com.example.mobileapp_project.sensorHandler.SensorItemCard

@Composable
fun SlaveScreen(viewModel: ChannelsViewModel, bluetoothHelper: BluetoothHelper) {
    val channels by viewModel.channels.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Slave Mode - Channels", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(channels) { channel ->
                SensorItemCard(name = channel.name, type = channel.sensorType)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        // Start data collection using BluetoothHelper
                        bluetoothHelper.startDataCollection()
                    }) {
                        Text("Start Data Collection")
                    }
                    Button(onClick = {
                        // Stop data collection using BluetoothHelper
                        bluetoothHelper.stopDataCollection()
                    }) {
                        Text("Stop Data Collection")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /* Logic for connecting via Bluetooth */ }) {
            Text("Connect to Master")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Add navigation logic to go back to previous screen if needed
        }) {
            Text("Back")
        }
    }
}
