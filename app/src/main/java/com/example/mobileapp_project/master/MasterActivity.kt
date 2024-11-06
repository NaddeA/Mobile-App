// MasterActivity.kt
package com.example.mobileapp_project.master

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mobileapp_project.bluetooth.BluetoothHelper
import com.example.mobileapp_project.ui.screens.MasterScreen

class MasterActivity : ComponentActivity() {

    private lateinit var bluetoothHelper: BluetoothHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bluetoothHelper = BluetoothHelper(this)
        bluetoothHelper.requestPermissions()

        setContent {
            if (bluetoothHelper.isBluetoothEnabled()) {
                MasterScreen(bluetoothHelper = bluetoothHelper, onBack = { finish() })
            } else {
                bluetoothHelper.toggleBluetooth()
            }
        }
    }
}
