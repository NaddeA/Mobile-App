package com.example.mobileapp_project.master

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp_project.bluetooth.BluetoothClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


import com.example.mobileapp_project.bluetooth.BluetoothHelper

class MasterViewModel(private val bluetoothHelper: BluetoothHelper) : ViewModel() {

    private val _connectionStatus = MutableStateFlow("Disconnected")
    val connectionStatus: StateFlow<String> = _connectionStatus

    private val _receivedData = MutableStateFlow("No Data")
    val receivedData: StateFlow<String> = _receivedData
    lateinit var bluetoothClient : BluetoothClient
    fun connectToSlave() {
        viewModelScope.launch {
            if (bluetoothClient.connectToSlave()) {
                _connectionStatus.value = "Connected"
            } else {
                _connectionStatus.value = "Failed to Connect"
            }
        }
    }

    fun sendCommand(command: String) {
        viewModelScope.launch {
            if (bluetoothClient.sendCommand(command)) {
                _receivedData.value = bluetoothClient.receiveResponse() ?: "No response"
            } else {
                _receivedData.value = "Failed to send command"
            }
        }
    }

    fun disconnect() {
        bluetoothClient.closeConnection()
    }
}
