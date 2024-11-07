package com.example.mobileapp_project

import android.app.Activity
import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.UUID

@SuppressLint("MissingPermission")
class BluetoothHelper(private val context: Context) {
    private val bluetoothAdapter: BluetoothAdapter? = getBluetoothAdapter()
    private val discoveredDevices = mutableListOf<BluetoothDevice>()
    private var bluetoothStateReceiver = BluetoothStateReceiver()

    init {
        // Register the Bluetooth state receiver
        val filter = IntentFilter().apply {
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
        }
        context.registerReceiver(bluetoothStateReceiver,filter)
    }

    // Get Bluetooth adapter based on Android version
    private fun getBluetoothAdapter(): BluetoothAdapter? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            bluetoothManager.adapter
        } else {
            BluetoothAdapter.getDefaultAdapter()
        }
    }

    // Enable or disable Bluetooth
//    @SuppressLint("MissingPermission")
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


    // Discover devices
//    @SuppressLint("MissingPermission")
    fun discoverDevices() {
        //Check permissions first because of the suppressLint
        if (!isPermissionGranted()) {
            requestPermissions()
            return
        }

        bluetoothAdapter?.let { adapter ->
            if (adapter.isDiscovering) {
                adapter.cancelDiscovery()
            }

            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND).apply {
                addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            }

            // Register for device discovery broadcasts
            context.registerReceiver(deviceDiscoveryReceiver, filter)

            val started = adapter.startDiscovery()
            if (started) {
                Log.d("BluetoothHelper", "Device discovery started successfully")
            } else {
                Log.d("BluetoothHelper", "Failed to start device discovery")
            }
        }
    }


    private val handler = Handler(Looper.getMainLooper())
    private val discoveryRunnable = object : Runnable {
        override fun run() {
            discoverDevices()
            handler.postDelayed(this, 10000) // Re-run discovery every 10 seconds
        }
    }

    fun startPeriodicDiscovery() {
        handler.post(discoveryRunnable)
    }

    fun stopPeriodicDiscovery() {
        handler.removeCallbacks(discoveryRunnable)
        bluetoothAdapter?.cancelDiscovery()
    }


    // Get paired devices
//    @SuppressLint("MissingPermission")
    fun getPairedDevices(): List<BluetoothDevice> {
        if (!isPermissionGranted()) {
            requestPermissions()
            return emptyList()
        }

        return bluetoothAdapter?.bondedDevices?.toList() ?: emptyList()
    }

    // Set discoverability
    fun enableDiscoverability(duration: Int = 300) {
        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
            putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, duration)
        }
        context.startActivity(discoverableIntent)
    }

    suspend fun connectToDevice(connectMe: BluetoothDevice){
        if (!isPermissionGranted()){
            requestPermissions()
            return
        }
        //this is the standard UUID (Universally Unique Identifier) for SSP (Serial Port Profile)
        val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        withContext(Dispatchers.IO) {
            val device = bluetoothAdapter?.getRemoteDevice(connectMe.address)
            try {  val socket = device?.createRfcommSocketToServiceRecord(uuid)
                bluetoothAdapter?.cancelDiscovery() // Cancel discovery to improve connection speed
                socket?.connect() // Blocking call
                // Connection succeeded, proceed with communication
            } catch (e: IOException) {
                Log.e("BluetoothHelper", "Connection to ${connectMe.name} failed", e)
                // Handle the failure, potentially with retries or user feedback
            }}

//        val bluetoothSocket : BluetoothSocket?
//
//        try {
//
//            bluetoothAdapter?.cancelDiscovery()
//            bluetoothSocket.connect()
//            Log.d("BluetoothHelper", "Connected to ${device.name}")
//        }
//        catch (e:IOException){
//            Log.e("BluetoothHelper" ,"Connection to ${device.name} Failed",e)
//            return
//        }
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

    // Register Bluetooth state receiver
    fun registerBluetoothReceiver() {
        bluetoothStateReceiver = BluetoothStateReceiver()
        val filter = IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
        context.registerReceiver(bluetoothStateReceiver, filter)
    }

    // Unregister Bluetooth state receiver
    fun unregisterBluetoothReceiver() {
        bluetoothStateReceiver.let {
            context.unregisterReceiver(it)
        }
    }

    // BroadcastReceiver for device discovery
    private val deviceDiscoveryReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action ?: return
            when (action) {
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Log.d("BluetoothHelper", "Discovery Started")
                    discoveredDevices.clear()
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Log.d("BluetoothHelper", "Discovery Finished")
                }
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
                    } else {
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    }
                    device?.let {
                        discoveredDevices.add(it)
                        Log.d("BluetoothHelper", "Device found: ${it.name ?: "Unknown"} - ${it.address}")
                    }
                }
            }
        }
    }


    fun createDeviceDiscoveryReceiver(onDeviceFound: (BluetoothDevice) -> Unit): BroadcastReceiver {
        val deviceDiscoveryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.action
                if (action == BluetoothDevice.ACTION_FOUND) {
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        onDeviceFound(it)
                    }
                }
            }
        }
        // Register the receiver with the necessary filter
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(deviceDiscoveryReceiver, filter)

        return deviceDiscoveryReceiver
    }

    // Unregister the Bluetooth state receiver when done
    fun unregisterReceiver(receiver: BroadcastReceiver) {
        context.unregisterReceiver(receiver)
    }

    // Get list of discovered devices
    fun getDiscoveredDevices(): List<BluetoothDevice> {
        return discoveredDevices
    }


    fun isBluetoothOn():Boolean{
        if (!isPermissionGranted()) {
            requestPermissions()
        }
        bluetoothAdapter?.let {  adapter ->
            return adapter.isEnabled
        }
        return false
    }

    companion object {
        fun getDiscoveredDeviceNames(bluetoothHelper: BluetoothHelper): List<String> { // need some rework incase permission not granted

            if (!bluetoothHelper.isPermissionGranted()) {
                bluetoothHelper.requestPermissions()

            }
            return bluetoothHelper.discoveredDevices.map { it.name ?: "Unknown Device" }
        }
    }
    // Inner class to handle Bluetooth state changes
    private inner class BluetoothStateReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            when (action) {
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                    when (state) {
                        BluetoothAdapter.STATE_OFF -> Log.d("BluetoothState", "Bluetooth is off")
                        BluetoothAdapter.STATE_TURNING_OFF -> Log.d("BluetoothState", "Bluetooth is turning off")
                        BluetoothAdapter.STATE_ON -> Log.d("BluetoothState", "Bluetooth is on")
                        BluetoothAdapter.STATE_TURNING_ON -> Log.d("BluetoothState", "Bluetooth is turning on")
                    }
                }
                BluetoothAdapter.ACTION_SCAN_MODE_CHANGED -> {
                    val mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR)
                    when (mode) {
                        BluetoothAdapter.SCAN_MODE_CONNECTABLE -> Log.d("BluetoothState", "Bluetooth is connectable")
                        BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE -> Log.d("BluetoothState", "Bluetooth is discoverable")
                        BluetoothAdapter.SCAN_MODE_NONE -> Log.d("BluetoothState", "Bluetooth is not connectable or discoverable")
                    }
                }
            }
        }
    }


}