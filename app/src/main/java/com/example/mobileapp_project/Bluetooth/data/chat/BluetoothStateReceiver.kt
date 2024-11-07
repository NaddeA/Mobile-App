package com.example.mobileapp_project.Bluetooth.data.chat

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

// BluetoothStateReceiver listens for Bluetooth connection state changes (connected/disconnected).
class BluetoothStateReceiver(
    private val onStateChanged: (isConnected: Boolean, BluetoothDevice) -> Unit // Callback triggered when the connection state changes
) : BroadcastReceiver() {

    // This method is triggered when a broadcast message is received.
    override fun onReceive(context: Context?, intent: Intent?) {

        // Retrieve the Bluetooth device from the intent (using different methods based on SDK version).
        val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(
                BluetoothDevice.EXTRA_DEVICE,
                BluetoothDevice::class.java
            )
        } else {
            intent?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        }

        // Check the action in the intent to determine the connection state change.
        when (intent?.action) {
            // If the device is connected, trigger the callback with 'true' for isConnected.
            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                onStateChanged(true, device ?: return)
            }
            // else trigger the callback with 'false' for isConnected.
            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                onStateChanged(false, device ?: return)
            }
        }
    }
}
