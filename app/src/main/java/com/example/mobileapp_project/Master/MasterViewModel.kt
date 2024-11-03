package com.example.mobileapp_project.Master



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp_project.Bluetooth.BluetoothClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MasterViewModel(private val bluetoothClient: BluetoothClient) : ViewModel() {

    private val _connectionStatus = MutableStateFlow("Disconnected")
    val connectionStatus: StateFlow<String> = _connectionStatus

    private val _receivedData = MutableStateFlow("No Data")
    val receivedData: StateFlow<String> = _receivedData

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
            bluetoothClient.sendCommand(command)
            _receivedData.value = bluetoothClient.receiveResponse() ?: "No response"
        }
    }

    fun disconnect() {
        bluetoothClient.closeConnection()
    }
}
{
}