package com.example.mobileapp_project.bluetoothj

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class Discoverability : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        if (action == BluetoothAdapter.ACTION_SCAN_MODE_CHANGED) {
            when (intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR)) {
                BluetoothAdapter.SCAN_MODE_CONNECTABLE -> {
                    Log.d("Discoverability", "Device is connectable but not discoverable.")
                }
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE -> {
                    Log.d("Discoverability", "Device is discoverable and connectable.")
                }
                BluetoothAdapter.SCAN_MODE_NONE -> {
                    Log.d("Discoverability", "Device is neither connectable nor discoverable.")
                }
            }
        }
    }
}
