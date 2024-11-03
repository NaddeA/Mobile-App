package com.example.mobileapp_project.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SlaveScreen(slaveViewModel: SlaveViewModel = viewModel()) {
    val sensorData by slaveViewModel.sensorData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sensor Data: $sensorData")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { slaveViewModel.handleCommand("[d]") }) {
            Text("Start Data Collection")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { slaveViewModel.handleCommand("[e]") }) {
            Text("Send Data to Master")
        }
    }
}
