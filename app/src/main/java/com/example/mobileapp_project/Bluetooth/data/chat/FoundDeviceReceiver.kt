package com.example.mobileapp_project.Bluetooth.data.chat

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

// FoundDeviceReceiver listens for Bluetooth device discovery events (when a device is found).
class FoundDeviceReceiver(
    private val onDeviceFound: (BluetoothDevice) -> Unit // Callback triggered when a new Bluetooth device is found
) : BroadcastReceiver() {

    // This method is triggered when a broadcast message is received.
    override fun onReceive(context: Context?, intent: Intent?) {
        // Check if the action corresponds to a device being found.
        when (intent?.action) {
            BluetoothDevice.ACTION_FOUND -> {
                // Retrieve the Bluetooth device from the intent (using different methods based on SDK version).
                val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(
                        BluetoothDevice.EXTRA_DEVICE,
                        BluetoothDevice::class.java
                    )
                } else {
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                }
                // If a device is found, trigger the onDeviceFound callback.
                device?.let(onDeviceFound)
            }
        }
    }
}
