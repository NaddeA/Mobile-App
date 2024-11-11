package com.example.mobileapp_project

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


// Old bluetooth functional class can be used now for checking bluetooth status, turning on and off, and discoverability

class BluetoothHelper(private val context: Context, private val bluetoothAdapter: BluetoothAdapter?) {

    companion object {
        const val REQUEST_BLUETOOTH_PERMISSIONS = 1001
        private const val TAG = "BluetoothHelper"
    }

    fun isBluetoothOn(): Boolean = bluetoothAdapter?.isEnabled == true

    fun enableBluetooth(): Boolean = bluetoothAdapter?.isEnabled == true

    fun hasBluetoothScanPermission(): Boolean = hasPermission(Manifest.permission.BLUETOOTH_SCAN)

    fun hasBluetoothConnectPermission(): Boolean = hasPermission(Manifest.permission.BLUETOOTH_CONNECT)

    @SuppressLint("MissingPermission")
    fun discoverDevices(activity: Activity, onDeviceFound: (BluetoothDevice) -> Unit) {
        if (!hasBluetoothScanPermission()) {
            requestPermission(activity, Manifest.permission.BLUETOOTH_SCAN)
            return
        }

        bluetoothAdapter?.apply {
            if (isDiscovering) cancelDiscovery()
            try {
                startDiscovery()
                // Register a BroadcastReceiver to handle discovered devices here
            } catch (e: SecurityException) {
                Log.e(TAG, "SecurityException while starting discovery: ${e.message}")
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getPairedDevices(activity: Activity): List<BluetoothDevice> {
        if (!hasBluetoothConnectPermission()) {
            requestPermission(activity, Manifest.permission.BLUETOOTH_CONNECT)
            return emptyList()
        }

        return bluetoothAdapter?.bondedDevices?.toList().orEmpty().also {
            if (it.isEmpty()) {
                Log.i(TAG, "No paired devices found.")
            }
        }
    }

    private fun requestPermission(activity: Activity, permission: String) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            Log.e(TAG, "Permission $permission is required for Bluetooth operation.")
        }
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(permission),
            REQUEST_BLUETOOTH_PERMISSIONS
        )
    }

    private fun hasPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}
