package com.example.mobileapp_project.slave

import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp_project.bluetooth.BluetoothServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class SlaveViewModel(
    private val context: Context,
    private val bluetoothServer: BluetoothServer
) : ViewModel() {

    private val _sensorDataList = MutableStateFlow<List<String>>(emptyList())
    val sensorDataList: StateFlow<List<String>> = _sensorDataList

    private val appSensorManager: AppSensorManager = AppSensorManager(context) { sensorType, x, y, z ->
        val sensorData = "$sensorType: X=$x, Y=$y, Z=$z"
        _sensorDataList.value = _sensorDataList.value + sensorData
    }

    private var bluetoothSocket: BluetoothSocket? = null

    /**
     * Starts sensor data collection through AppSensorManager.
     */
    fun startDataCollection() {
        viewModelScope.launch(Dispatchers.IO) {
            appSensorManager.startDataCollection()
            Log.d("SlaveViewModel", "Sensor data collection started.")
        }
    }

    /**
     * Stops sensor data collection through AppSensorManager.
     */
    fun stopDataCollection() {
        viewModelScope.launch(Dispatchers.IO) {
            appSensorManager.stopDataCollection()
            Log.d("SlaveViewModel", "Sensor data collection stopped.")
        }
    }

    /**
     * Sends the latest collected data to the master device over Bluetooth.
     */
    fun sendDataToMaster() {
        viewModelScope.launch(Dispatchers.IO) {
            val latestData = appSensorManager.getLoggedData() // Retrieve logged data
            bluetoothSocket?.let {
                bluetoothServer.sendResponse("DATA: $latestData", it)
                Log.d("SlaveViewModel", "Sent data to master: $latestData")
            } ?: Log.e("SlaveViewModel", "Bluetooth socket is null. Cannot send data.")
        }
    }

    /**
     * Handles commands received from the master device.
     */
    fun handleCommand(command: String, socket: BluetoothSocket) {
        viewModelScope.launch(Dispatchers.IO) {
            when {
                command.startsWith("[a,") -> {
                    val freq = command.substring(3, command.length - 1) // Extract frequency
                    bluetoothServer.sendResponse("FREQ=$freq OK", socket)
                    Log.d("SlaveViewModel", "Handled command: Set frequency to $freq")
                }
                command == "[d]" -> {
                    startDataCollection() // Start sampling
                    bluetoothServer.sendResponse("SAMPLING... OK", socket)
                }
                command.startsWith("[c,") -> {
                    // Fetch data for a specific channel, e.g., "[c,5]" to request channel 5
                    val channel = command.substring(3, command.length - 1).toIntOrNull()
                    if (channel != null) {
                        val data = appSensorManager.getChannelData(channel) ?: 0f
                        bluetoothServer.sendResponse("DATA: Channel $channel = $data", socket)
                        Log.d("SlaveViewModel", "Handled command: Send data for channel $channel")
                    } else {
                        bluetoothServer.sendResponse("ERROR: Invalid channel", socket)
                        Log.w("SlaveViewModel", "Invalid channel requested: $command")
                    }
                }
                command == "[e]" -> {
                    sendDataToMaster() // Send logged data to master
                }
                else -> {
                    bluetoothServer.sendResponse("ERROR: UNKNOWN COMMAND", socket)
                    Log.w("SlaveViewModel", "Received unknown command: $command")
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch(Dispatchers.IO) {
            stopDataCollection() // Stop data collection when ViewModel is cleared
            Log.d("SlaveViewModel", "ViewModel cleared. Data collection stopped.")
        }
    }
}
