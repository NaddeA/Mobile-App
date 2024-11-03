package com.example.mobileapp_project.ui.master

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobileapp_project.master.MasterViewModel

@Composable
fun MasterScreen(masterViewModel: MasterViewModel = viewModel()) {
    val connectionStatus by masterViewModel.connectionStatus.collectAsState()
    val receivedData by masterViewModel.receivedData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Connection Status: $connectionStatus")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { masterViewModel.connectToSlave() }) {
            Text("Connect to Slave")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { masterViewModel.sendCommand("[a,1000]") }) {
            Text("Set Sampling Rate (1000 Hz)")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { masterViewModel.sendCommand("[d]") }) {
            Text("Start Sampling")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { masterViewModel.sendCommand("[e]") }) {
            Text("Send Sampled Data")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Received Data: $receivedData")
    }
}
