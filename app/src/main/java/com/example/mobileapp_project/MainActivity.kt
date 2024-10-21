package com.example.mobileapp_project

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
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
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mobileapp_project.ui.theme.MobileAppprojectTheme

class MainActivity : ComponentActivity() {
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var receiver: BluetoothReceiver
    private lateinit var receiver2: Discoverability
    private var discoveredDevices = mutableListOf<String>()  // Declare discovered devices list
    private lateinit var bluetoothEnableLauncher: ActivityResultLauncher<Intent>

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up Bluetooth
        bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        receiver = BluetoothReceiver()
        receiver2 = Discoverability()

        // Req perm if necessary
        val requiredPermissions = arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

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

        // Set Compose content
        setContent {
            MobileAppprojectTheme {
                MainScreen(
                    bluetoothAdapter = bluetoothAdapter,
                    onBluetoothToggle = { enableDisableBluetooth() },
                    onDiscoverDevices = { discoverDevices() },
                    onGetPairedDevices = { getPairedDevices() },
                    onDiscoverability = { discoverability() },
                    discoveredDevices = discoveredDevices  // Pass discovered devices to UI
                )
            }
        }
    }

    // Bluetooth device discovery logic
    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("MissingPermission")
    private fun discoverDevices() {
        // Check for permission before starting discovery
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1
            )
            return
        }

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
                        Log.d("Bluetooth", "Device found: $deviceName - $deviceAddress")
                        discoveredDevices.add("$deviceName - $deviceAddress")
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    private fun enableDisableBluetooth() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                1
            )
            return
        }

        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            bluetoothEnableLauncher.launch(enableBtIntent)
        } else {
            //make sure that android later than 13 disconnects bluetooth manually.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Toast.makeText(this, "Please disable Bluetooth manually from settings", Toast.LENGTH_LONG).show()
                Log.d(
                    "Bluetooth",
                    "Bluetooth can't be turned off programmatically on Android 13+. Please disable Bluetooth manually."
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getPairedDevices() {
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices
        pairedDevices?.forEach { device ->
            Log.d("PairedDevices", "${device.name} - ${device.address}")
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    private fun discoverability() {
        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        startActivity(discoverableIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}

@Composable
fun MainScreen(
    bluetoothAdapter: BluetoothAdapter,
    onBluetoothToggle: () -> Unit,
    onDiscoverDevices: () -> Unit,
    onGetPairedDevices: () -> Unit,
    onDiscoverability: () -> Unit,
    discoveredDevices: List<String>  // List to display discovered devices
) {
    var bluetoothEnabled by remember { mutableStateOf(bluetoothAdapter.isEnabled) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            onBluetoothToggle()
            bluetoothEnabled = !bluetoothEnabled
        }) {
            Text(if (bluetoothEnabled) "Disable Bluetooth" else "Enable Bluetooth")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onDiscoverDevices() }) {
            Text("Discover Devices")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Discovered Devices:")
        discoveredDevices.forEach { device ->
            Text(device)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onGetPairedDevices() }) {
            Text("Get Paired Devices")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onDiscoverability() }) {
            Text("Make Discoverable")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MobileAppprojectTheme {
        MainScreen(
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(),
            onBluetoothToggle = {},
            onDiscoverDevices = {},
            onGetPairedDevices = {},
            onDiscoverability = {},
            discoveredDevices = emptyList()  // Provide a default empty list
        )
    }
}
