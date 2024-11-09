package com.example.mobileapp_project.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobileapp_project.SensorItem
import com.example.mobileapp_project.SensorType
import com.example.mobileapp_project.SensorList

@Composable
fun MainScreen(
    isBluetoothEnabled: Boolean,
    onEnableBluetoothClick: () -> Unit,
    onActivateMasterModeClick: () -> Unit,
    onActivateSlaveModeClick: () -> Unit,
    //onSensorClick: (SensorType) -> Unit,
    sensorList: List<SensorItem>,
    onSensorItemClick: (SensorItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text("Bluetooth Controller") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isBluetoothEnabled) "Bluetooth is Enabled" else "Bluetooth is Disabled",
                fontSize = 20.sp,
                color = if (isBluetoothEnabled) MaterialTheme.colors.primary else MaterialTheme.colors.error
            )

            Button(
                onClick = onEnableBluetoothClick,
                enabled = !isBluetoothEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enable Bluetooth")
            }

            Button(
                onClick = onActivateMasterModeClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Activate Master Mode")
            }

            Button(
                onClick = onActivateSlaveModeClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Activate Slave Mode")
            }

            // Section for general sensor control buttons (like temperature or humidity)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Sensor Controls", style = MaterialTheme.typography.h6)



            // Sensor List section - use the SensorList composable
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Available Sensors", style = MaterialTheme.typography.h6)

            SensorList(
                sensorList = sensorList,
                onItemClick = onSensorItemClick
            )
        }
    }
}
