package com.example.mobileapp_project
//
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.bluetooth.BluetoothAdapter
//import android.bluetooth.BluetoothDevice
//import android.bluetooth.BluetoothManager
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.content.IntentFilter
//import android.content.pm.PackageManager
//import android.os.Build
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.result.ActivityResultLauncher
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.example.mobileapp_project.ui.theme.MobileAppprojectTheme
//
//
//class OldMain : ComponentActivity() {
//    private lateinit var bluetoothAdapter: BluetoothAdapter
//    private lateinit var bluetoothManager: BluetoothManager
//    private lateinit var receiver: BluetoothReceiver
//    private lateinit var discoverabilityReceiver: Discoverability
//    var discoveredDevices = mutableListOf<String>()
//    private lateinit var bluetoothEnableLauncher: ActivityResultLauncher<Intent>
//
//
//    super.onCreate(savedInstanceState)
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//    // Set up Bluetooth Adapter depending on Android version
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//        bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
//        bluetoothAdapter = bluetoothManager.adapter
//    } else {
//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
//    }
//
//    receiver = BluetoothReceiver()
//    discoverabilityReceiver = Discoverability()
//
//    // Request permissions if necessary
//    val requiredPermissions = getRequiredPermissions()
//    if (!requiredPermissions.all {
//        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
//    }) {
//        ActivityCompat.requestPermissions(this, requiredPermissions, 1)
//    }
//
//    // Register for Bluetooth enable result
//    bluetoothEnableLauncher = registerForActivityResult(
//    ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        if (result.resultCode == RESULT_OK) {
//            Log.d("Bluetooth", "Bluetooth enabled")
//        } else {
//            Log.d("Bluetooth", "Bluetooth enabling denied")
//        }
//    }
//    registerReceiver(discoverDeviceReceiver, IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED))
//
//
//    setContent {
//        MobileAppprojectTheme {
//            MainScreen(
//                onBluetoothToggle = { enableDisableBluetooth() },
//                onDiscoverDevices = { discoverDevices() },
//                onGetPairedDevices = { getPairedDevices() },
//                onDiscoverability = { discoverability() },
//                discoveredDevices = discoveredDevices
//            )
//        }
//    }
//}
//
//// Function to determine permissions required based on Android version
//private fun getRequiredPermissions(): Array<String> {
//    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//        arrayOf(
//            Manifest.permission.BLUETOOTH_SCAN,
//            Manifest.permission.BLUETOOTH_CONNECT,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        )
//    } else {
//        arrayOf(
//            Manifest.permission.BLUETOOTH,
//            Manifest.permission.BLUETOOTH_ADMIN,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        )
//    }
//}
//
//@SuppressLint("MissingPermission")
//private fun discoverDevices() {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
//            != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.ACCESS_FINE_LOCATION),
//                1
//            )
//            return
//        }
//    } else {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
//            != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_FINE_LOCATION),
//                1
//            )
//            return
//        }
//    }
//
//    if (bluetoothAdapter.isDiscovering) {
//        bluetoothAdapter.cancelDiscovery()
//    }
//
//    val filter = IntentFilter(BluetoothDevice.ACTION_FOUND).apply {
//        addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
//        addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
//    }
//    registerReceiver(discoverDeviceReceiver, filter)
//
//    val started = bluetoothAdapter.startDiscovery()
//    if (started) {
//        Log.d("Bluetooth", "Device discovery started successfully")
//    } else {
//        Log.d("Bluetooth", "Failed to start device discovery")
//    }
//}
//
//private val discoverDeviceReceiver = object : BroadcastReceiver() {
//    @SuppressLint("MissingPermission")
//    override fun onReceive(context: Context?, intent: Intent?) {
//        val action = intent?.action ?: return
//        when (action) {
//            BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
//                Log.d("Bluetooth", "Discovery Started")
//            }
//            BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
//                Log.d("Bluetooth", "Discovery Finished")
//            }
//            BluetoothDevice.ACTION_FOUND -> {
//                val device: BluetoothDevice? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
//                } else {
//                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
//                }
//                device?.let {
//                    val deviceName = "Samsun24S"
//                    val deviceAddress = "Kristianstad"
//                    //val deviceName = device.name ?: "Unknown Device"
//                    //val deviceAddress = device.address
//                    Log.d("Bluetooth", "Device found: $deviceName - $deviceAddress")
//                    discoveredDevices.add("$deviceName - $deviceAddress")
//                }
//            }
//        }
//    }
//}
//
//@SuppressLint("MissingPermission")
//private fun enableDisableBluetooth() {
//    val permissionGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//        ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
//    } else {
//        ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED
//    }
//
//    if (!permissionGranted) {
//        ActivityCompat.requestPermissions(
//            this,
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                arrayOf(Manifest.permission.BLUETOOTH_CONNECT)
//            } else {
//                arrayOf(Manifest.permission.BLUETOOTH)
//            },
//            1
//        )
//        return
//    }
//
//    if (!bluetoothAdapter.isEnabled) {
//        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//        bluetoothEnableLauncher.launch(enableBtIntent)
//    } else {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            Toast.makeText(this, "Please disable Bluetooth manually from settings", Toast.LENGTH_LONG).show()
//            Log.d("Bluetooth", "Bluetooth can't be turned off programmatically on Android 13+. Please disable manually.")
//        } else {
//            bluetoothAdapter.disable()
//            Log.d("Bluetooth", "Bluetooth disabled programmatically")
//        }
//    }
//}
//
//@SuppressLint("MissingPermission")
//private fun getPairedDevices() {
//    if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
//            1
//        )
//        return
//    }
//
//    val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
//    pairedDevices?.forEach { device ->
//        Log.d("PairedDevices", "${device.name} - ${device.address}")
//    }
//}
//
//@SuppressLint("MissingPermission")
//private fun discoverability() {
//    if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//        ActivityCompat.requestPermissions(
//            this,
//            arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
//            1
//        )
//        return
//    }
//
//    val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
//    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
//    startActivity(discoverableIntent)
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MobileAppprojectTheme {
//        MainScreen(
//            onBluetoothToggle = {},
//            onDiscoverDevices = {},
//            onGetPairedDevices = {},
//            onDiscoverability = {},
//            discoveredDevices = emptyList()
//        )
//    }
//}
//}
//
