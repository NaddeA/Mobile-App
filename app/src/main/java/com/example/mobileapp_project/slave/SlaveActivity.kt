package com.example.mobileapp_project.slave

import android.Manifest
import android.bluetooth.BluetoothSocket
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mobileapp_project.bluetooth.BluetoothServer
import com.example.mobileapp_project.sensorHandler.SensorManagerHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SlaveActivity : ComponentActivity() {

    private lateinit var bluetoothServer: BluetoothServer
    private lateinit var sensorManagerHelper: SensorManagerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request necessary Bluetooth permissions
        requestBluetoothPermissions()

        // Initialize SensorManagerHelper and BluetoothServer with safe context usage
        sensorManagerHelper = SensorManagerHelper(this)
        bluetoothServer = BluetoothServer(this) { command, socket ->
            handleCommand(command, socket)
        }

        // Start accepting Bluetooth connections within a coroutine
        CoroutineScope(Dispatchers.IO).launch {
            bluetoothServer.startAcceptingConnections()
        }
    }

    private fun requestBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12+
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN),
                    1
                )
            }
        } else { // Android 11 and below
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            }
        }
    }

    private fun handleCommand(command: String, socket: BluetoothSocket) {
        when {
            command.startsWith("[a,") -> {
                val freq = command.substring(3, command.length - 1) // Extract frequency
                bluetoothServer.sendResponse("FREQ=$freq OK", socket)
            }
            command == "[d]" -> {
                sensorManagerHelper.startDataCollection(Sensor.TYPE_ACCELEROMETER)
                bluetoothServer.sendResponse("SAMPLING... OK", socket)
            }
            command == "[e]" -> {
                val data = "DATA: ${sensorManagerHelper.getLoggedData()}"
                bluetoothServer.sendResponse(data, socket)
            }
            else -> {
                bluetoothServer.sendResponse("UNKNOWN COMMAND", socket)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothServer.closeConnection()
        sensorManagerHelper.stopDataCollection()
    }
}
