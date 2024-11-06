package com.example.mobileapp_project.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID

class BluetoothServerViewModel(private val context: Context) : ViewModel() {

    private val _connectionStatus = MutableStateFlow("Disconnected")
    val connectionStatus: StateFlow<String> = _connectionStatus
    private var bluetoothSocket: BluetoothSocket? = null
    private var serverSocket: BluetoothServerSocket? = null
    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val deviceUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    init {
        startServer()
    }

    @SuppressLint("MissingPermission")
    private fun startServer() {
        if (!hasBluetoothPermissions()) {
            Log.e("BluetoothServerViewModel", "Bluetooth permissions are not granted.")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(
                    "BluetoothServer",
                    deviceUUID
                )
                _connectionStatus.value = "Waiting for Connection"
                while (true) {
                    val socket = serverSocket?.accept() ?: break
                    _connectionStatus.value = "Connected"
                    bluetoothSocket = socket
                    handleClient(socket)
                }
            } catch (e: IOException) {
                Log.e("BluetoothServerViewModel", "Error accepting connection", e)
                _connectionStatus.value = "Error"
            }
        }
    }

    fun sendResponse(command: String) {
        bluetoothSocket?.let { socket ->
            try {
                val outputStream = socket.outputStream
                outputStream.write(command.toByteArray())
                outputStream.flush()
            } catch (e: IOException) {
                Log.e("BluetoothServerViewModel", "Error sending response", e)
            }
        }
    }

    private fun handleClient(socket: BluetoothSocket) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputStream = socket.inputStream
                val buffer = ByteArray(1024)
                val bytes = inputStream.read(buffer)
                val command = String(buffer, 0, bytes)
                Log.d("BluetoothServer", "Received command: $command")

                // Send acknowledgment or handle command
                socket.outputStream.write("ACK: $command".toByteArray())
            } catch (e: IOException) {
                Log.e("BluetoothServerViewModel", "Error handling client", e)
            } finally {
                socket.close()
            }
        }
    }

    private fun hasBluetoothPermissions(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_ADMIN
        )
        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onCleared() {
        super.onCleared()
        try {
            serverSocket?.close()
        } catch (e: IOException) {
            Log.e("BluetoothServerViewModel", "Error closing server socket", e)
        }
    }
}
