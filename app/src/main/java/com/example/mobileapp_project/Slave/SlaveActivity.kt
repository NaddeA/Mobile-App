package com.example.mobileapp_project.slave

import android.bluetooth.BluetoothSocket
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.mobileapp_project.bluetooth.BluetoothServer
import com.example.mobileapp_project.SensorHandler.SensorManagerHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SlaveActivity : ComponentActivity() {

    private lateinit var bluetoothServer: BluetoothServer
    private lateinit var sensorManagerHelper: SensorManagerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManagerHelper = SensorManagerHelper(this) { data ->
            // Callback for sensor data
            // You could broadcast this data if necessary
        }

        bluetoothServer = BluetoothServer(this) { command, socket ->
            handleCommand(command, socket)
        }

        CoroutineScope(Dispatchers.IO).launch {
            bluetoothServer.startAcceptingConnections()
        }
    }

    private fun handleCommand(command: String, socket: BluetoothSocket) {
        when {
            command.startsWith("[a,") -> {
                val response = "FREQ=1000 OK"
                bluetoothServer.sendResponse(response, socket)
            }
            command == "[d]" -> {
                sensorManagerHelper.startDataCollection()
                val response = "SAMPLING... OK"
                bluetoothServer.sendResponse(response, socket)
            }
            command == "[e]" -> {
                val data = "DATA: [sampled data]" // Format your actual sensor data here
                bluetoothServer.sendResponse(data, socket)
            }
            else -> {
                val response = "UNKNOWN COMMAND"
                bluetoothServer.sendResponse(response, socket)
            }
        }
    }
}
