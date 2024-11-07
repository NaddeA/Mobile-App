package com.example.mobileapp_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mobileapp_project.bluetooth.BluetoothHelper


class MainActivity : ComponentActivity() {

    private lateinit var bluetoothHelper: BluetoothHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize BluetoothHelper
        bluetoothHelper = BluetoothHelper(this)
        bluetoothHelper.requestPermissions() // Ensure Bluetooth permissions at start

        setContent {
            AppNavigator(bluetoothHelper = bluetoothHelper)
        }
    }
}
