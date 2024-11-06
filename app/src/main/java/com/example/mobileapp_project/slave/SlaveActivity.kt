// SlaveActivity.kt
package com.example.mobileapp_project.slave

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mobileapp_project.bluetooth.BluetoothHelper
import com.example.mobileapp_project.ui.component.RequestBluetoothPermissions

class SlaveActivity : ComponentActivity() {

    private lateinit var bluetoothHelper: BluetoothHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize BluetoothHelper
        bluetoothHelper = BluetoothHelper(this)

        setContent {
            RequestBluetoothPermissions {
                if (bluetoothHelper.isBluetoothEnabled()) {
                    // Continue with the setup, such as starting data collection
                    // Example: bluetoothHelper.startDataCollection()
                } else {
                    bluetoothHelper.toggleBluetooth() // Enable Bluetooth if not enabled
                }
            }
        }
    }
}
