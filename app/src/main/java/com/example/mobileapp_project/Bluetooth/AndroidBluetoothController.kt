package com.example.mobileapp_project.Bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.core.app.ActivityCompat

@SuppressLint("MissingPermission")
class AndroidBluetoothController(
    private val context:Context
) : BluetoothController {


    private companion object {
        const val REQUEST_BLUETOOTH_PERMISSION = 1
    }


    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val _scannedDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())

    override val scannedDevices: StateFlow<List<BluetoothDevice>>
        get() = _scannedDevices.asStateFlow()

    private val _pairedDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())

    override val pairedDevices: StateFlow<List<BluetoothDevice>>
        get() = _pairedDevices.asStateFlow()


    init {
        updatePairedDevices()
    }

    override fun startDiscovery() {
        if (!havePermission(Manifest.permission.BLUETOOTH_SCAN)) {
            return
        }
        updatePairedDevices()
        bluetoothAdapter?.startDiscovery()
    }

    override fun stopDiscovery() {
        TODO("Not yet implemented")
    }

    override fun release() {
        TODO("Not yet implemented")
    }


    private fun updatePairedDevices() {
        if (!havePermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            return
        }
        bluetoothAdapter
            ?.bondedDevices
            ?.also { devices ->
                _pairedDevices.update { devices.toList() }
            }
    }

    // General Purpose permission checker
    private fun havePermission(permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val hasBluetoothPermission =
                context.checkSelfPermission(Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
            val hasAdminPermission =
                context.checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED // Corrected variable name
            return hasBluetoothPermission && hasAdminPermission
        }
        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED // For lower versions, check the specific permission
    }

    private fun requestBluetoothPermissions() {
        if (context is Activity) { // Check if context is an Activity
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN),
                REQUEST_BLUETOOTH_PERMISSION
            )
        }
    }

}