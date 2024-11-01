package com.example.mobileapp_project.Master


import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileapp_project.Bluetooth.BluetoothClient
import java.io.IOException

class MasterActivity : AppCompatActivity() {

    private lateinit var bluetoothClient: BluetoothClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val deviceAddress = "slave-device-address" // Replace with the MAC address of the slave

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
}
