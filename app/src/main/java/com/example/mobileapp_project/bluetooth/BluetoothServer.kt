package com.example.mobileapp_project.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.OutputStream
import java.util.*

class BluetoothServer(
    private val context: Context,
    private val bluetoothAdapter: BluetoothAdapter?,
    private val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // Default UUID for Bluetooth SPP
) {

    private var serverSocket: BluetoothServerSocket? = null
    private var isRunning = true

    @SuppressLint("MissingPermission")
    fun startAcceptingConnections(onCommandReceived: (String, BluetoothSocket) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                serverSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord("BluetoothServer", uuid)
                Log.d("BluetoothServer", "Waiting for connection...")

                while (isRunning) {
                    val socket = serverSocket?.accept()
                    socket?.let {
                        Log.d("BluetoothServer", "Connection accepted from ${it.remoteDevice.name}")
                        listenForCommands(it, onCommandReceived)
                    }
                }
            } catch (e: IOException) {
                Log.e("BluetoothServer", "Error accepting connection: ${e.message}")
            } finally {
                closeConnection()
            }
        }
    }

    private fun listenForCommands(socket: BluetoothSocket, onCommandReceived: (String, BluetoothSocket) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val inputStream = socket.inputStream
            val buffer = ByteArray(1024)
            while (isRunning) {
                try {
                    val bytesRead = inputStream.read(buffer)
                    val command = String(buffer, 0, bytesRead)
                    onCommandReceived(command, socket)
                } catch (e: IOException) {
                    Log.e("BluetoothServer", "Error reading command: ${e.message}")
                    break
                }
            }
        }
    }

    fun sendResponse(response: String, socket: BluetoothSocket) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val outputStream: OutputStream = socket.outputStream
                outputStream.write(response.toByteArray())
                outputStream.flush()
                Log.d("BluetoothServer", "Sent response: $response")
            } catch (e: IOException) {
                Log.e("BluetoothServer", "Error sending response: ${e.message}")
            }
        }
    }

    fun closeConnection() {
        isRunning = false
        try {
            serverSocket?.close()
            Log.d("BluetoothServer", "Server socket closed")
        } catch (e: IOException) {
            Log.e("BluetoothServer", "Error closing server socket: ${e.message}")
        }
    }
}
