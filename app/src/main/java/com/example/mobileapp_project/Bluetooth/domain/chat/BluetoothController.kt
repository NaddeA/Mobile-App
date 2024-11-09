package com.example.mobileapp_project.Bluetooth.domain.chat

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {

    val isConnected: StateFlow<Boolean> //connection status
    //  List of paired/ Scanned Devices
    val scannedDevices: StateFlow<List<BluetoothDevice>>
    val pairedDevices: StateFlow<List<BluetoothDevice>>

    val errors: SharedFlow<String>  // List of errors
    fun sendCommand(command: String): Flow<CommandResult>
    fun receiveResponse(): Flow<String>
    // search / Stop searching
    fun startDiscovery()
    fun stopDiscovery()

    // Starts a Bluetooth server to accept incoming connections.
    fun startBluetoothServer(): Flow<ConnectionResult>

    // Connects to a specific Bluetooth device.
    fun connectToDevice(device: BluetoothDevice): Flow<ConnectionResult>

    // Tries to send a message over Bluetooth, returns the sent message if successful.
    suspend fun trySendMessage(message: String): BluetoothMessage?

    // garbageMan
    fun closeConnection()
    fun release()


}

sealed interface CommandResult {
    object CommandSent : CommandResult
    data class ResponseReceived(val response: String) : CommandResult
    data class Error(val message: String) : CommandResult
}