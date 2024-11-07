package com.example.mobileapp_project.bluetoothj

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BluetoothReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        if (action == BluetoothAdapter.ACTION_SCAN_MODE_CHANGED) {
            when (intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR)) {
                BluetoothAdapter.SCAN_MODE_CONNECTABLE -> {
                    Log.d("BluetoothReceiver", "Bluetooth is connectable.")
                }
                BluetoothAdapter.STATE_TURNING_OFF -> {
                    Log.d("BluetoothReceiver", "Bluetooth is turning off.")
                }
                BluetoothAdapter.STATE_ON -> {
                    Log.d("BluetoothReceiver", "Bluetooth is on.")
                }
                BluetoothAdapter.STATE_TURNING_ON -> {
                    Log.d("BluetoothReceiver", "Bluetooth is turning on.")
                }
            }
        }
    }
}
