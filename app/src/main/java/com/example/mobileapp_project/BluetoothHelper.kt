package com.example.mobileapp_project

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


// Old bluetooth functional class can be used now for checking bluetooth status, turning on and off, and discoverability
@SuppressLint("MissingPermission")
class BluetoothHelper(private val context: Context, private val bluetoothAdapter: BluetoothAdapter?) {
    private lateinit var enableBluetoothLauncher: ActivityResultLauncher<Intent>

    companion object {
        const val REQUEST_BLUETOOTH_PERMISSIONS = 1001
        const val REQUEST_ENABLE_BT = 1002

        private const val TAG = "BluetoothHelper"
    }

    fun isBluetoothOn(): Boolean = bluetoothAdapter?.isEnabled == true

    fun hasBluetoothScanPermission(): Boolean = hasPermission(Manifest.permission.BLUETOOTH_SCAN)

    fun hasBluetoothConnectPermission(): Boolean = hasPermission(Manifest.permission.BLUETOOTH_CONNECT)


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

    // make this actually turn the bluetooth on

    fun enableBluetooth(activity: Activity) {
        if (isBluetoothOn()) return
        if (!isPermissionGranted()) {
            requestPermissions()
            return
        }
        bluetoothAdapter?.let { adapter ->
            if (!adapter.isEnabled) {
                val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                context.startActivity(enableIntent)
            } else {
                return}
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

    fun toggleBluetooth() {
        if (!isPermissionGranted()) {
            requestPermissions()
            return
        }
        bluetoothAdapter?.let { adapter ->
            if (!adapter.isEnabled) {
                val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                context.startActivity(enableIntent)
            } else {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    adapter.disable()
                } else {
                    Log.d("BluetoothHelper", "Bluetooth cannot be turned off programmatically on Android 13+.")
                }
            }
        }
    }
    // Helper to check Bluetooth permissions
    private fun isPermissionGranted(): Boolean {
        val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }

        return requiredPermissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Request Bluetooth permissions
    private fun requestPermissions() {
        if (context is Activity) {
            val requiredPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } else {
                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
            ActivityCompat.requestPermissions(context, requiredPermissions, 1)
        }
    }


}
