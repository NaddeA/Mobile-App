// BluetoothClient.kt
package com.example.mobileapp_project.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.util.*

class BluetoothClient(
    private val device: BluetoothDevice,
    private val deviceUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
) {
    private var bluetoothSocket: BluetoothSocket? = null



    @SuppressLint("MissingPermission")
    fun connectToSlave(): Boolean {
        return try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(deviceUUID)
            bluetoothSocket?.connect()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }


    fun sendCommand(command: String): Boolean {
        return try {
            bluetoothSocket?.outputStream?.write(command.toByteArray())
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun receiveResponse(): String? {
        return try {
            val buffer = ByteArray(1024)
            val bytes = bluetoothSocket?.inputStream?.read(buffer) ?: 0
            String(buffer, 0, bytes)
        } catch (e: IOException) {
            e.printStackTrace()
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
