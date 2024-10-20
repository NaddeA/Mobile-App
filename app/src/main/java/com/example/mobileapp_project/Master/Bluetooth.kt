package com.example.mobileapp_project.Master

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat


class BluetoothHelper(private val context: Context) {
    val bluetoothAvailable = context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
    val bluetoothLEAvailable = context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
}



//val bluetoothManager = ContextCompat.getSystemService(this,Context.BLUETOOTH_SERVICE) as? BluetoothManager
//val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.getAdapter()
