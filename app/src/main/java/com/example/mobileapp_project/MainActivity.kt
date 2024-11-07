package com.example.mobileapp_project

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
class MainActivity : ComponentActivity() {


    private lateinit var bluetoothHelper: BluetoothHelper
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Initialize BluetoothHelper with the current context
    bluetoothHelper = BluetoothHelper(this)

    // Register the Bluetooth state receiver
    bluetoothHelper.registerBluetoothReceiver()

    setContent(){
        val discovered: List<BluetoothDevice> = bluetoothHelper.getDiscoveredDevices()
        val paired: List<BluetoothDevice> = bluetoothHelper.getPairedDevices()
        var currentScreen by remember { mutableStateOf<Screen>(Screen.MainMenu) }
//
        AppScreen(bluetoothHelper)

    }
}

    private fun toggleBluetooth() {
        bluetoothHelper.toggleBluetooth()
    }

    private fun discoverDevices() {
        bluetoothHelper.discoverDevices()
    }

    @SuppressLint("MissingPermission")
    private fun getPairedDevices() {
        val pairedDevices = bluetoothHelper.getPairedDevices() //this could cause an Issue because of the permission required for the name in 12+ but for now it will be left assuming the Bluetooth helper class takes care of it
        pairedDevices.forEach { device ->
            Log.d("PairedDevices", "Device: ${device.name} - ${device.address}")
        }
    }

    private fun enableDiscoverability() {
        bluetoothHelper.enableDiscoverability()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the Bluetooth state receiver when activity is destroyed
        bluetoothHelper.unregisterBluetoothReceiver()
    }
}

