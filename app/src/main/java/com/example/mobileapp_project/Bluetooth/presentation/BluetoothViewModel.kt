package com.example.mobileapp_project.Bluetooth.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapp_project.Bluetooth.domain.chat.BluetoothController
import com.example.mobileapp_project.Bluetooth.domain.chat.BluetoothDeviceDomain
import com.example.mobileapp_project.Bluetooth.domain.chat.BluetoothMessage
import com.example.mobileapp_project.Bluetooth.domain.chat.CommandResult
import com.example.mobileapp_project.Bluetooth.domain.chat.ConnectionResult
import com.example.mobileapp_project.Sensor.SensorItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BluetoothViewModel @Inject constructor(
    private val bluetoothController: BluetoothController
): ViewModel() {
    var isMaster: Boolean by mutableStateOf(false)

    fun setRoleAsMaster() {
        isMaster = true
        bluetoothController.startBluetoothServer().listen() // Server listens for connections from slaves
    }
    fun setRoleAsSlave(device: BluetoothDeviceDomain) {
        isMaster = false
        bluetoothController.connectToDevice(device).listen()
    }
    suspend fun sendMasterCommand(command: String) {
        if (isMaster) {
            bluetoothController.sendCommand(command).collect {
                when (it) {
                    is CommandResult.CommandSent -> { /* Command sent */ }
                    is CommandResult.Error -> { /* Show error */ }
                    else -> { /* Handle other results */ }
                }
            }
        }
    }


    // Holds the UI state of the Bluetooth screen.
    private val _state = MutableStateFlow(BluetoothUiState())

    // Combines device and connection states for the UI.
    val state = combine(
        bluetoothController.scannedDevices,
        bluetoothController.pairedDevices,
        _state
    ) { scannedDevices, pairedDevices, state ->
        state.copy(
            scannedDevices = scannedDevices,
            pairedDevices = pairedDevices,
            messages = if(state.isConnected) state.messages else emptyList()
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)

    private var deviceConnectionJob: Job? = null


    init {
        // Listen for Bluetooth connection status changes
        bluetoothController.isConnected.onEach { isConnected ->
            _state.update { it.copy(isConnected = isConnected) }
        }.launchIn(viewModelScope)

        // Listen for error messagess from the bluetooth controller
        bluetoothController.errors.onEach { error ->
            _state.update { it.copy(
                errorMessage = error
            ) }
        }.launchIn(viewModelScope)
    }

    //  Connect to a specified device and update the state
    fun connectToDevice(device: BluetoothDeviceDomain) {
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = bluetoothController
            .connectToDevice(device)
            .listen()
    }

    //  Disconnect
    fun disconnectFromDevice() {
        deviceConnectionJob?.cancel()
        bluetoothController.closeConnection()
        _state.update {
            it.copy(
                isConnecting = false,
                isConnected = false
            )
        }
    }

    // listen for connection
    fun waitForIncomingConnections() {
        _state.update { it.copy(isConnecting = true) }
        deviceConnectionJob = bluetoothController
            .startBluetoothServer()
            .listen()
    }

    //send a message to connected device
    fun sendMessage(message: String) {
        viewModelScope.launch {
            val bluetoothMessage = bluetoothController.trySendMessage(message)
            if(bluetoothMessage != null) {
                _state.update { it.copy(
                    messages = it.messages + bluetoothMessage
                ) }
            }
        }
    }


    fun sendData(sensorItem:SensorItem,sensorData:String) {
        viewModelScope.launch {
            val collectedData = mutableListOf<String>()
            var sampleCount = 0
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val sensorName = sensorItem.title
            val formattedData = "$timestamp - Sensor: $sensorName, Value: $sensorData"
            collectedData.add(formattedData)

            sampleCount++

            // Send the data when the sample count reaches or exceeds the threshold
            var bluetoothMessage: BluetoothMessage? = null
            if (sampleCount >= 10) {
                val result = collectedData.joinToString("\n")
                bluetoothMessage = bluetoothController.trySendMessage(result)

                // Check if the message was successfully sent
                if (bluetoothMessage != null) {
                    _state.update {
                        it.copy(
                            messages = it.messages + bluetoothMessage
                        )
                    }
                } else {
                    // Optionally handle if sending failed
                }
            }
        }
    }

    // Start or stop device discovery.
    fun startScan() = bluetoothController.startDiscovery()
    fun stopScan() = bluetoothController.stopDiscovery()

    // this just helps handle connection results
    private fun Flow<ConnectionResult>.listen(): Job {
        return onEach { result ->
            when(result) {
                ConnectionResult.ConnectionEstablished -> {
                    _state.update { it.copy(
                        isConnected = true,
                        isConnecting = false,
                        errorMessage = null
                    ) }
                }
                is ConnectionResult.TransferSucceeded -> {
                    _state.update { it.copy(
                        messages = it.messages + result.message
                    ) }
                }
                is ConnectionResult.Error -> {
                    _state.update { it.copy(
                        isConnected = false,
                        isConnecting = false,
                        errorMessage = result.message
                    ) }
                }
            }
        }
            .catch { throwable ->
                bluetoothController.closeConnection()
                _state.update { it.copy(
                    isConnected = false,
                    isConnecting = false,
                ) }
            }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        bluetoothController.release()
    }
}