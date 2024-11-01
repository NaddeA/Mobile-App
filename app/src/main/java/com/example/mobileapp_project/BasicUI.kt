//package com.example.mobileapp_project
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Button
//import androidx.compose.material3.Divider
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun MainScreen(
//    onBluetoothToggle: () -> Unit,
//    onDiscoverDevices: () -> Unit,
//    onGetPairedDevices: () -> Unit,
//    onDiscoverability: () -> Unit,
//    discoveredDevices: List<String>
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Button(onClick = onBluetoothToggle) {
//            Text("Toggle Bluetooth")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(onClick = onDiscoverDevices) {
//            Text("Discover Devices")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(onClick = onGetPairedDevices) {
//            Text("Get Paired Devices")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(onClick = onDiscoverability) {
//            Text("Make Discoverable")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text("Discovered Devices:")
//        DeviceListScreen(deviceNames = discoveredDevices) { }
//    }
//}
//@Composable
//fun DeviceListScreen(
//    deviceNames: List<String>,
//    onDeviceClick: (String) -> Unit
//) {
//    LazyColumn {
//        items(deviceNames) { deviceName ->
//            Text(
//                text = deviceName,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//                    .clickable { onDeviceClick(deviceName) },
//                style = MaterialTheme.typography.bodySmall
//            )
//            HorizontalDivider()  // Optional: adds a line between items
//        }
//    }
//}
//@Composable
//fun BluetoothDevicesScreen(bluetoothHelper: BluetoothHelper) {
//    val deviceNames = BluetoothHelper.getDiscoveredDeviceNames(bluetoothHelper)
//
//    DeviceListScreen(deviceNames) { selectedDevice ->
//        // Define your action here when a device is clicked
//        println("Device clicked: $selectedDevice")
//        // e.g., connect to the selected device or open a detailed view
//    }
//}
//
//
package com.example.mobileapp_project

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
    discoveredDevices: List<BluetoothDevice>,
    pairedDevices: List<BluetoothDevice>
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
        DeviceListScreen(deviceNames = discoveredDevices) { selectedDevice ->
            // Handle discovered device click
            println("Discovered Device clicked: $selectedDevice")
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()

        Text("Paired Devices:")
        DeviceListScreen(deviceNames = pairedDevices) { selectedDevice   ->
            // Handle paired device click
            println("Paired Device clicked: $selectedDevice")
        }

        Spacer(modifier = Modifier.height(16.dp))


    }
}

@SuppressLint("MissingPermission")
@Composable
fun DeviceListScreen(
    deviceNames: List<BluetoothDevice>,
    onDeviceClick: (String) -> Unit
) {
    LazyColumn {
        items(deviceNames) { deviceName ->
            Text(
                text = deviceName.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
//                    .clickable { onDeviceClick(deviceName) }
                ,
                style = MaterialTheme.typography.bodyLarge // Adjusted for better visibility
            )
            HorizontalDivider() // Adds a line between items
        }
    }
}

