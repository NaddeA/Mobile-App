package com.example.mobileapp_project

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobileapp_project.master.MasterViewModel
import com.example.mobileapp_project.slave.SlaveViewModel
import java.util.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions() // Request necessary permissions at the start

        setContent {
            SensorApp()
        }
    }

    @Composable
    fun SensorApp() {
        var mode by remember { mutableStateOf<Mode?>(null) }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when (mode) {
                null -> ModeSelectionScreen(onModeSelected = { selectedMode ->
                    mode = selectedMode
                })

                Mode.Master -> MasterScreen()
                Mode.Slave -> SlaveScreen()
            }
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // Check for API level 23+
            val permissions = mutableListOf<String>()
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Check for Android 12+ specific permissions
                if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
                }
                if (checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.BLUETOOTH_SCAN)
                }
            } else { // Android 11 and below permissions
                if (checkSelfPermission(Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.BLUETOOTH)
                }
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
            if (permissions.isNotEmpty()) {
                requestPermissions(permissions.toTypedArray(), 1)
            }
        }
    }
}

@Composable
fun ModeSelectionScreen(onModeSelected: (Mode) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Select Mode", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onModeSelected(Mode.Master) },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("Master Mode")
        }

        Button(
            onClick = { onModeSelected(Mode.Slave) },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("Slave Mode")
        }
    }
}

@Composable
fun MasterScreen() {
    val masterViewModel: MasterViewModel = viewModel()
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

@Composable
fun SlaveScreen() {
    val slaveViewModel: SlaveViewModel = viewModel()
    val sensorDataList = slaveViewModel.sensorDataList

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sensor Data:")

        Spacer(modifier = Modifier.height(8.dp))

        Column {
            sensorDataList.forEach { data ->
                Text(text = data, style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { slaveViewModel.startDataCollection() }) {
            Text("Start Data Collection")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // Assuming the `sendDataToMaster` needs a BluetoothSocket which must be initialized
            // Replace `socket` with actual socket if available
            slaveViewModel.sendDataToMaster()
        }) {
            Text("Send Data to Master")
        }
    }
}

enum class Mode {
    Master, Slave
}
