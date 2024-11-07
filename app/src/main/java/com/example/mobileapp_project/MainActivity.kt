package com.example.mobileapp_project

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts


import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mobileapp_project.bluetoothj.BluetoothReceiver
import com.example.mobileapp_project.bluetoothj.Discoverability
import com.example.mobileapp_project.ui.theme.MainScreen
import com.example.mobileapp_project.ui.theme.MobileAppprojectTheme
import java.util.*

class MainActivity : ComponentActivity() {
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var receiver: BluetoothReceiver
    private lateinit var discoverabilityReceiver: Discoverability
    private var discoveredDevices = mutableListOf<String>()
    private lateinit var bluetoothEnableLauncher: ActivityResultLauncher<Intent>
    private val discoveredDevicesMap = mutableMapOf<String, BluetoothDevice>()
    private var connectedSocket: BluetoothSocket? = null
    private val MY_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up Bluetooth Adapter depending on Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            bluetoothAdapter = bluetoothManager.adapter
        } else {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        }

        receiver = BluetoothReceiver()
        discoverabilityReceiver = Discoverability()

        // Request permissions if necessary
        val requiredPermissions = getRequiredPermissions()
        if (!requiredPermissions.all {
                ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
            }) {
            ActivityCompat.requestPermissions(this, requiredPermissions, 1)
        }

        // Register for Bluetooth enable result
        bluetoothEnableLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d("Bluetooth", "Bluetooth enabled")
            } else {
                Log.d("Bluetooth", "Bluetooth enabling denied")
            }
        }

        // Register receiver for discoverability changes
        registerReceiver(discoverabilityReceiver, IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED))

        setContent {
            MobileAppprojectTheme {
                MainScreen(
                    onBluetoothToggle = { enableDisableBluetooth() },
                    onDiscoverDevices = { discoverDevices() },
                    onGetPairedDevices = { getPairedDevices() },
                    onDiscoverability = { discoverability() },
                    onStartAcceptingConnections = { startAcceptingConnections() },
                    discoveredDevices = discoveredDevices,
                    onConnectToDevice = { deviceInfo -> connectToDevice(deviceInfo) }
                )
            }
        }
    }

    // Function to determine permissions required based on Android version
    private fun getRequiredPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
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
    }

    @SuppressLint("MissingPermission")
    private fun discoverDevices() {
        if (bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.cancelDiscovery()
        }

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND).apply {
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        }
        registerReceiver(discoverDeviceReceiver, filter)

        val started = bluetoothAdapter.startDiscovery()
        if (started) {
            Log.d("Bluetooth", "Device discovery started successfully")
        } else {
            Log.d("Bluetooth", "Failed to start device discovery")
        }
    }

    private val discoverDeviceReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action ?: return
            when (action) {
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Log.d("Bluetooth", "Discovery Started")
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Log.d("Bluetooth", "Discovery Finished")
                }
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        val deviceName = device.name ?: "Unknown Device"
                        val deviceAddress = device.address
                        discoveredDevicesMap[deviceAddress] = device
                        discoveredDevices.add("$deviceName - $deviceAddress")
                        Log.d("Bluetooth", "Device found: $deviceName - $deviceAddress")
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun connectToDevice(deviceInfo: String) {
        val deviceAddress = deviceInfo.split(" - ")[1]
        val device = discoveredDevicesMap[deviceAddress]

        if (device != null) {
            val socket = device.createRfcommSocketToServiceRecord(MY_UUID)
            bluetoothAdapter.cancelDiscovery()  // Cancel discovery to save resources
            try {
                socket.connect()
                connectedSocket = socket
                Log.d("Bluetooth", "Connected to device: ${device.name}")
            } catch (e: Exception) {
                Log.e("Bluetooth", "Failed to connect: ${e.message}")
                try {
                    socket.close()
                } catch (closeException: Exception) {
                    Log.e("Bluetooth", "Could not close socket: ${closeException.message}")
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableDisableBluetooth() {
        val permissionGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
        }

        if (!permissionGranted) {
            ActivityCompat.requestPermissions(
                this,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    arrayOf(Manifest.permission.BLUETOOTH_CONNECT)
                } else {
                    arrayOf(Manifest.permission.BLUETOOTH)
                },
                1
            )
            return
        }

        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            bluetoothEnableLauncher.launch(enableBtIntent)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Toast.makeText(this, "Please disable Bluetooth manually from settings", Toast.LENGTH_LONG).show()
                Log.d("Bluetooth", "Bluetooth can't be turned off programmatically on Android 13+. Please disable manually.")
            } else {
                bluetoothAdapter.disable()
                Log.d("Bluetooth", "Bluetooth disabled programmatically")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getPairedDevices() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                1
            )
            return
        }

        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
        pairedDevices?.forEach { device ->
            Log.d("PairedDevices", "${device.name} - ${device.address}")
        }
    }

    @SuppressLint("MissingPermission")
    private fun discoverability() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                1
            )
            return
        }

        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        startActivity(discoverableIntent)
    }

    @SuppressLint("MissingPermission")
    private fun startAcceptingConnections() {
        val serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("BluetoothApp", MY_UUID)
        val thread = Thread {
            try {
                val socket = serverSocket.accept()
                connectedSocket = socket
                Log.d("Bluetooth", "A device has connected: ${socket.remoteDevice.name}")
            } catch (e: Exception) {
                Log.e("Bluetooth", "Failed to accept connection: ${e.message}")
            }
        }
        thread.start()
    }
}
