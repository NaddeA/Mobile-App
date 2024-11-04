package com.example.mobileapp_project.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

class BluetoothServer(
    private val context: Context,
    private val onCommandReceived: (String, BluetoothSocket) -> Unit
) {
    private val bluetoothAdapter: BluetoothAdapter = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
    private val deviceUUID: UUID = UUID.fromString("your-uuid-here")
    private var serverSocket: BluetoothServerSocket? = null

    suspend fun startAcceptingConnections() = withContext(Dispatchers.IO) {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e("BluetoothServer", "BLUETOOTH_CONNECT permission not granted")
            return@withContext
        }

        try {
            serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("SlaveService", deviceUUID)
            while (true) {
                val socket: BluetoothSocket? = serverSocket?.accept()
                socket?.let {
                    handleConnection(it)
                    serverSocket?.close() // Close after accepting one connection
                }
            }
        } catch (e: IOException) {
            Log.e("BluetoothServer", "Error starting connection: ${e.message}")
        }
    }

    private fun handleConnection(socket: BluetoothSocket) {
        val inputStream = socket.inputStream
        val buffer = ByteArray(1024)
        try {
            val bytes = inputStream.read(buffer)
            val command = String(buffer, 0, bytes)
            onCommandReceived(command, socket)
        } catch (e: IOException) {
            Log.e("BluetoothServer", "Error handling connection: ${e.message}")
        }
    }

    fun sendResponse(response: String, socket: BluetoothSocket) {
        try {
            socket.outputStream.write(response.toByteArray())
        } catch (e: IOException) {
            Log.e("BluetoothServer", "Error sending response: ${e.message}")
        }
    }

    fun closeConnection() {
        try {
            serverSocket?.close()
            serverSocket = null
            Log.d("BluetoothServer", "Server socket closed")
        } catch (e: IOException) {
            Log.e("BluetoothServer", "Error closing server socket: ${e.message}")
        }
    }
}
