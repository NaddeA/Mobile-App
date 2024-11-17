package com.example.mobileapp_project.ui.theme.UI

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mobileapp_project.Bluetooth.presentation.BluetoothUiState
import com.example.mobileapp_project.Sensor.SensorItem



@Composable
fun SlaveDeviceScreen(
    sensorList: List<SensorItem>,
    onSensorItemClick: (SensorItem) -> Unit,
    state: BluetoothUiState,
    onStartServer: ()-> Unit,
    onBack: ()-> Unit
) {
    Box {

        Column(
            modifier = Modifier,

            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box (modifier = Modifier.height(500.dp).fillMaxWidth().border(2.dp, Color.Blue)){
                SensorList(
                    sensorList = sensorList,
                    onItemClick = onSensorItemClick
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = { onStartServer() }) {
                    Text("StartServer")
                }
                Button(onClick = { onBack() }) {
                    Text("Back")
                }

            }
        }
    }
}
