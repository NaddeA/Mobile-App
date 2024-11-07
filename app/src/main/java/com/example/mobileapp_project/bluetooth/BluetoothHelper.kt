// BluetoothHelper.kt
package com.example.mobileapp_project.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.*
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

class BluetoothHelper(private val context: Context) {
    private val bluetoothAdapter: BluetoothAdapter? = getBluetoothAdapter()
    private val discoveredDevices = mutableListOf<BluetoothDevice>()
    private var connectionCallback: ((BluetoothDevice, BluetoothSocket?) -> Unit)? = null
    private var deviceDiscoveryReceiver: BroadcastReceiver? = null
    private var bluetoothSocket: BluetoothSocket? = null // Socket for communication

    private fun getBluetoothAdapter(): BluetoothAdapter? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            bluetoothManager.adapter
        } else {
            BluetoothAdapter.getDefaultAdapter()
        }
    }

    // Check if Bluetooth is enabled
    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled == true
    }

    // Toggle Bluetooth state
    @SuppressLint("MissingPermission")
    fun toggleBluetooth() {
        if (!isPermissionGranted()) {
            requestPermissions()
            return
        }
        bluetoothAdapter?.let { adapter ->
            if (!adapter.isEnabled) {
                val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                if (context is Activity) {
                    context.startActivity(enableIntent)
                }
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                adapter.disable() // Bluetooth can't be disabled programmatically on Android 13+
            }
        }
    }

    // Check required Bluetooth permissions
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
    fun requestPermissions() {
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

    // Start Bluetooth discovery
    @SuppressLint("MissingPermission")
    fun startDiscovery() {
        if (!isPermissionGranted()) {
            requestPermissions()
            return
        }
        bluetoothAdapter?.let { adapter ->
            if (adapter.isDiscovering) {
                adapter.cancelDiscovery()
            }
            adapter.startDiscovery()
            Log.d("BluetoothHelper", "Discovery started")
        }
    }

    // Stop Bluetooth discovery
    @SuppressLint("MissingPermission")
    fun stopDiscovery() {
        bluetoothAdapter?.takeIf { it.isDiscovering }?.cancelDiscovery()
        Log.d("BluetoothHelper", "Discovery stopped")
    }

    // Connect to a specified Bluetooth device

    @SuppressLint("MissingPermission")
    fun connectToDevice(device: BluetoothDevice, callback: (BluetoothDevice, BluetoothSocket?) -> Unit): Boolean {
        if (!isPermissionGranted()) {
            requestPermissions()
            return false // Permissions not granted, return false
        }

        connectionCallback = callback
        stopDiscovery() // Stop discovery before connecting

        return try {
            val socket = device.createRfcommSocketToServiceRecord(SERVICE_UUID)
            socket.connect()
            bluetoothSocket = socket // Set the connected socket
            callback(device, socket) // Notify successful connection
            true // Connection successful, return true
        } catch (e: IOException) {
            Log.e("BluetoothHelper", "Connection failed", e)
            callback(device, null) // Notify failed connection
            false // Connection failed, return false
        }
    }


    // Retrieve paired devices
    @SuppressLint("MissingPermission")
    fun getPairedDevices(): List<BluetoothDevice> {
        if (!isPermissionGranted()) {
            requestPermissions()
            return emptyList()
        }
        return bluetoothAdapter?.bondedDevices?.toList() ?: emptyList()
    }

    // Set a listener for device discovery
    fun setDeviceDiscoveryCallback(onDeviceFound: (BluetoothDevice) -> Unit) {
        if (deviceDiscoveryReceiver == null) {
            deviceDiscoveryReceiver = object : BroadcastReceiver() {
                @SuppressLint("MissingPermission")
                override fun onReceive(context: Context, intent: Intent) {
                    val action = intent.action
                    if (BluetoothDevice.ACTION_FOUND == action) {
                        val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                        device?.let {
                            if (!discoveredDevices.contains(it)) {
                                discoveredDevices.add(it)
                                onDeviceFound(it)  // Notify when a new device is found
                            }
                        }
                    }
                }
            }
            context.registerReceiver(deviceDiscoveryReceiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
        }
    }

    // Start data collection (stub for integration with sensors or other data sources)
    fun startDataCollection() {
        Log.d("BluetoothHelper", "Data collection started")
        // Add real data collection logic here
    }

    // Stop data collection (stub for integration with sensors or other data sources)
    fun stopDataCollection() {
        Log.d("BluetoothHelper", "Data collection stopped")
        // Add real data collection stop logic here
    }

    // Send a command to the connected Bluetooth device
    fun sendCommand(command: String) {
        bluetoothSocket?.let { socket ->
            try {
                val outputStream: OutputStream = socket.outputStream
                outputStream.write(command.toByteArray())
                outputStream.flush()
                Log.d("BluetoothHelper", "Command sent: $command")
            } catch (e: IOException) {
                Log.e("BluetoothHelper", "Error sending command", e)
            }
        } ?: Log.e("BluetoothHelper", "No active Bluetooth connection to send command")
    }

    // Cleanup resources and unregister the receiver
    fun cleanup() {
        deviceDiscoveryReceiver?.let {
            context.unregisterReceiver(it)
            deviceDiscoveryReceiver = null
        }
        bluetoothSocket?.close()
    }

    companion object {
        private val SERVICE_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    }
}
