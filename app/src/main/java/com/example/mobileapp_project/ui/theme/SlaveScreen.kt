package com.example.mobileapp_project.ui.slave

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobileapp_project.slave.SlaveViewModel

@Composable
fun SlaveScreen(slaveViewModel: SlaveViewModel = viewModel()) {
    val sensorDataList = slaveViewModel.sensorDataList

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sensor Data: $sensorDataList")

        Spacer(modifier = Modifier.height(8.dp))

        Column {
            sensorDataList.forEach { data ->
                Text(text = data, style = MaterialTheme.typography.bodyLarge)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { slaveViewModel.startDataCollection()}) {
            Text("Start Data Collection")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { slaveViewModel.sendDataToMaster()}) {
            Text("Send Data to Master")
        }
    }
}
