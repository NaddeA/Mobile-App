package com.example.mobileapp_project.bluetooth
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.*

class BluetoothClient(private val device: BluetoothDevice) {

    private var bluetoothSocket: BluetoothSocket? = null
    private val deviceUUID: UUID = UUID.fromString("your-uuid-here")

    @SuppressLint("MissingPermission")
    fun connectToSlave(): Boolean {
        return try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(deviceUUID)
            bluetoothSocket?.connect()
            Log.d("BluetoothClient", "Connected to slave")
            true
        } catch (e: IOException) {
            Log.e("BluetoothClient", "Connection failed", e)
            closeConnection()
            false
        }
    }

    fun sendCommand(command: String): Boolean {
        return try {
            bluetoothSocket?.outputStream?.write(command.toByteArray())
            Log.d("BluetoothClient", "Sent command: $command")
            true
        } catch (e: IOException) {
            Log.e("BluetoothClient", "Failed to send command", e)
            Log.e("BluetoothClient", "Failed to send command", e)
            false
        }
    }

    fun receiveResponse(): String? {
        return try {
            val buffer = ByteArray(1024)
            val bytes = bluetoothSocket?.inputStream?.read(buffer) ?: 0
            String(buffer, 0, bytes)
        } catch (e: IOException) {
            Log.e("BluetoothClient", "Failed to receive response", e)
            null
        }
    }

    fun closeConnection() {
        try {
            bluetoothSocket?.close()
        } catch (e: IOException) {
            Log.e("BluetoothClient", "Failed to close connection", e)
        }
    }
}
