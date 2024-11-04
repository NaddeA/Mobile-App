package com.example.mobileapp_project.master

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileapp_project.bluetooth.BluetoothClient
import com.example.mobileapp_project.R

class MasterActivity : AppCompatActivity() {

    private lateinit var bluetoothClient: BluetoothClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bluetoothAdapter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
        } else {
            BluetoothAdapter.getDefaultAdapter()
        }

        val deviceAddress = "slave-device-address" // Replace with the actual MAC address
        val device = bluetoothAdapter.getRemoteDevice(deviceAddress)
        bluetoothClient = BluetoothClient(device)
        bluetoothClient.connectToSlave()
    }

    private fun sendCommand(command: String) {
        bluetoothClient.sendCommand(command)
    }

    private fun receiveResponse() {
        val response = bluetoothClient.receiveResponse()
        Log.d("Master", "Response from Slave: $response")
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothClient.closeConnection() // Close client socket directly
    }
}
