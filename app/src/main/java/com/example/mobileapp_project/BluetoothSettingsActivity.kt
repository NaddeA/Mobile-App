package com.example.mobileapp_project

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BluetoothSettingsActivity(private val context: Context) {

    private val bluetoothAdapter: BluetoothAdapter? by lazy { BluetoothAdapter.getDefaultAdapter() }

    @SuppressLint("MissingPermission")
    fun toggleBluetooth() {
        if (bluetoothAdapter == null) {
            Toast.makeText(context, "Bluetooth is not supported on this device", Toast.LENGTH_LONG).show()
            return
        }

        if (!bluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            context.startActivity(enableBtIntent)
        } else {
            Toast.makeText(context, "Bluetooth is already enabled", Toast.LENGTH_SHORT).show()
        }
    }

    fun setMasterMode() {
        Toast.makeText(context, "Master mode activated", Toast.LENGTH_SHORT).show()
    }

    fun setSlaveMode() {
        Toast.makeText(context, "Slave mode activated", Toast.LENGTH_SHORT).show()
    }
}
