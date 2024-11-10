package com.example.mobileapp_project

import BluetoothSettingsComposable
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import androidx.activity.compose.setContent
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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.mobileapp_project.ui.MainScreen
import com.example.mobileapp_project.ui.theme.MobileAppProjectTheme
import com.example.mobileapp_project.R

@RequiresApi(Build.VERSION_CODES.S)
class MainActivity : ComponentActivity() {

    // Rename the property to avoid naming conflict
    private val btAdapter by lazy {
        val bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothManager?.adapter
    }
    private val bluetoothHelper by lazy { BluetoothHelper(this, btAdapter) }
    private val sensorDetailManager by lazy { SensorDetailManager(this) }
    private lateinit var sensorManager: SensorManager
    private lateinit var logDataService: LogDataService

    private val enableBluetoothLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { /* Not needed */ }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize sensor manager and log data service
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        logDataService = LogDataService(this)

        // Set content view for the activity using Jetpack Compose
        setContent {
            MobileAppProjectTheme {
                var showBluetoothSettings by remember { mutableStateOf(false) }
                val sensorList by remember { mutableStateOf(getSensorList()) }

                // Show either the Bluetooth settings or the main screen
                if (showBluetoothSettings) {
                    BluetoothSettingsComposable(
                        isBluetoothEnabled = bluetoothHelper.isBluetoothOn(),
                        onEnableBluetoothClick = {
                            if (!bluetoothHelper.enableBluetooth()) {
                                requestEnableBluetooth()
                            }
                        },
                        onActivateMasterModeClick = {
                            Toast.makeText(this, "Master Mode Activated", Toast.LENGTH_SHORT).show()
                        },
                        onActivateSlaveModeClick = {
                            Toast.makeText(this, "Slave Mode Activated", Toast.LENGTH_SHORT).show()
                        },
                        onBackClick = {
                            showBluetoothSettings = false
                        }
                    )
                } else {
                    MainScreen(
                        isBluetoothEnabled = bluetoothHelper.isBluetoothOn(),
                        onEnableBluetoothClick = {
                            if (!bluetoothHelper.enableBluetooth()) {
                                requestEnableBluetooth()
                            }
                        },
                        onActivateMasterModeClick = {
                            Toast.makeText(this, "Master Mode Activated", Toast.LENGTH_SHORT).show()
                        },
                        onActivateSlaveModeClick = {
                            Toast.makeText(this, "Slave Mode Activated", Toast.LENGTH_SHORT).show()
                        },
                        sensorList = sensorList,
                        onSensorItemClick = { sensorItem ->
                            sensorDetailManager.registerSensor(sensorItem.sensorType) { sensorData ->
                                Toast.makeText(this, "Sensor Data from ${sensorItem.title}: $sensorData", Toast.LENGTH_SHORT).show()
                                logDataService.logSensorData(sensorItem.title, sensorData)
                            }
                        }
                    )
                }
            }
        }
    }

    // Function to request enabling Bluetooth
    private fun requestEnableBluetooth() {
        val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        enableBluetoothLauncher.launch(enableIntent)
    }
}


    // Function to get the list of sensors available on the device
    private fun getSensorList(): List<SensorItem> {
        return sensorManager.getSensorList(Sensor.TYPE_ALL).map { deviceSensor ->
            SensorItem(
                title = deviceSensor.name,
                description = "Type: ${deviceSensor.type}",
                icon = R.drawable.sensor, // Placeholder icon
                sensorType = deviceSensor.type
            )
        }
    }

    override fun onPause() {
        super.onPause()
        sensorDetailManager.unregisterSensor()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == BluetoothHelper.REQUEST_BLUETOOTH_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the Bluetooth operation
            } else {
                // Permission denied, show a message to the user
                Log.e("MainActivity", "Bluetooth permission was denied.")
            }
        }
    }
}
