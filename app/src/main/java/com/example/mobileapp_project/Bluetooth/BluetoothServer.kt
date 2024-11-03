package com.example.mobileapp_project.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

class BluetoothServer(private val context: Context, private val onCommandReceived: (String, BluetoothSocket) -> Unit) {

    private val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val deviceUUID: UUID = UUID.fromString("your-uuid-here") // Use the same UUID as in the Master

    @SuppressLint("MissingPermission")
    suspend fun startAcceptingConnections() = withContext(Dispatchers.IO) {
        val serverSocket: BluetoothServerSocket? = bluetoothAdapter.listenUsingRfcommWithServiceRecord("SlaveService", deviceUUID)

        while (true) {
            val socket: BluetoothSocket? = try {
                serverSocket?.accept()
            } catch (e: IOException) {
                Log.e("BluetoothServer", "Connection failed", e)
                break
            }

            socket?.let {
                handleConnection(it)
                serverSocket?.close()
                break
            }
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
            Log.e("BluetoothServer", "Failed to read command", e)
        }
    }

    fun sendResponse(response: String, socket: BluetoothSocket) {
        try {
            socket.outputStream.write(response.toByteArray())
        } catch (e: IOException) {
            Log.e("BluetoothServer", "Failed to send response", e)
        }
    }
}
