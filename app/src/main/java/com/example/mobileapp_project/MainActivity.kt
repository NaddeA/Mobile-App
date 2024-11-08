package com.example.mobileapp_project

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobileapp_project.ui.theme.MobileAppProjectTheme

@RequiresApi(Build.VERSION_CODES.S)
class MainActivity : ComponentActivity() {

    private val bluetoothManager by lazy {
        getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // SensorManager instansieras här och skickas till MainScreen
        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensorList = listOf(
            SensorItem(
                title = "Accelerometer",
                description = "Type: 1",
                icon = R.drawable.sensor, // Sätt din standardikon här
                sensorType = Sensor.TYPE_ACCELEROMETER
            ),
            SensorItem(
                title = "Gyroscope",
                description = "Type: 4",
                icon = R.drawable.sensor, // Sätt din standardikon här
                sensorType = Sensor.TYPE_GYROSCOPE
            ),

        )

        setContent {
            MobileAppProjectTheme {
                MainScreen(
                    sensorManager = sensorManager,
                    isBluetoothEnabled = isBluetoothEnabled,
                    onEnableBluetooth = { enableBluetooth() },
                    onSensorClick = { sensorItem ->
                        val intent = Intent(this, SensorDetailActivity::class.java).apply {
                            putExtra("sensor_name", sensorItem.title)
                            putExtra("sensor_type", sensorItem.sensorType)
                        }
                        startActivity(intent)
                    },
                    onBluetoothSettingsClick = {
                        startActivity(Intent(this, BluetoothSettingsActivity::class.java))
                    }
                )
            }
        }
    }

    private fun enableBluetooth() {
        val enableBluetoothLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // Här kan du hantera resultatet om det behövs
        }

        val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val canEnableBluetooth = permissions[Manifest.permission.BLUETOOTH_CONNECT] == true || Build.VERSION.SDK_INT < Build.VERSION_CODES.S
            if (canEnableBluetooth && !isBluetoothEnabled) {
                enableBluetoothLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionLauncher.launch(arrayOf(Manifest.permission.BLUETOOTH_CONNECT))
        } else if (!isBluetoothEnabled) {
            enableBluetoothLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        }
    }
}

// MainScreen composable
@Composable
fun MainScreen(
    sensorManager: SensorManager,
    isBluetoothEnabled: Boolean,
    onEnableBluetooth: () -> Unit,
    onSensorClick: (SensorItem) -> Unit,
    onBluetoothSettingsClick: () -> Unit
) {
    var sensorList by remember { mutableStateOf<List<SensorItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        sensorList = getSensorList(sensorManager)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onBluetoothSettingsClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Bluetooth Settings")
        }

        Text(
            "Available Sensors:",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        SensorList(sensorList, onSensorClick)
    }
}

// Flytta sensordatahämtningen till en separat funktion
fun getSensorList(sensorManager: SensorManager): List<SensorItem> {
    return sensorManager.getSensorList(Sensor.TYPE_ALL).map { deviceSensor ->
        SensorItem(
            title = deviceSensor.name,
            description = "Type: ${deviceSensor.type}",icon = android.R.drawable.ic_menu_info_details
            , // Placeholder icon
            sensorType = deviceSensor.type
        )
    }
}
