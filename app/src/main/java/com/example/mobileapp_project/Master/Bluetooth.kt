package com.example.mobileapp_project.Master

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat



val bluetoothAvailable = requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
val bluetoothLEAvailable = requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)


//val bluetoothManager = ContextCompat.getSystemService(this,Context.BLUETOOTH_SERVICE) as? BluetoothManager
//val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.getAdapter()
