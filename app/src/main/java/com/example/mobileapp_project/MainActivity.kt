package com.example.mobileapp_project

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import com.example.mobileapp_project.Sensor.SensorDetailManager
import com.example.mobileapp_project.Sensor.SensorItem
import com.example.mobileapp_project.ui.theme.UI.MainScreen
import com.example.mobileapp_project.ui.theme.MobileAppProjectTheme
import com.example.mobileapp_project.ui.theme.UI.BluetoothSettingsComposable

@RequiresApi(Build.VERSION_CODES.S)
class MainActivity : ComponentActivity() {

    // Rename the property to avoid naming conflict
    private val btAdapter by lazy {
        val bluetoothManager = getSystemService(BluetoothManager::class.java)
        bluetoothManager?.adapter
    }

    private val bluetoothHelper by lazy { BluetoothHelper(this, btAdapter) }

    // Keep in mind for Bluetooth The Data transfer service class takes a socket when initialized and sends/listens to sent messages
    private val sensorDetailManager by lazy { SensorDetailManager(this) }
    private lateinit var sensorManager: SensorManager
    private lateinit var logDataService: LogDataService

    private val enableBluetoothLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { /* Not needed */ }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        logDataService = LogDataService(this)
        // Initialize sensor manager and log data service

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
                            if (!bluetoothHelper.enableBluetooth(this@MainActivity)) {
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
                            if (!bluetoothHelper.enableBluetooth(this@MainActivity)) {
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
