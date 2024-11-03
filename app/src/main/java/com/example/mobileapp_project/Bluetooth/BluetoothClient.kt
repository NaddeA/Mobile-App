package com.example.mobileapp_project.Bluetooth
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
    fun connectToSlave() {
        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(deviceUUID)
            bluetoothSocket?.connect()
            Log.d("BluetoothClient", "Connected to slave")
        } catch (e: IOException) {
            Log.e("BluetoothClient", "Connection failed", e)
            closeConnection()
        }
    }

    fun sendCommand(command: String) {
        try {
            bluetoothSocket?.outputStream?.write(command.toByteArray())
        } catch (e: IOException) {
            Log.e("BluetoothClient", "Failed to send command", e)
        }
    }

    fun receiveResponse(): String? {
        val buffer = ByteArray(1024)
        return try {
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
