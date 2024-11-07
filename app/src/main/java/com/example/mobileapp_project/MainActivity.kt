package com.example.mobileapp_project

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : ComponentActivity() {

    @AndroidEntryPoint
    class MainActivity : AppCompatActivity() {
        private val bluetoothManager by lazy {
            applicationContext.getSystemService(BluetoothManager::class.java)
        }
        private val bluetoothAdapter by lazy {
            bluetoothManager?.adapter
        }

        private val isBluetoothEnabled: Boolean
            get() = bluetoothAdapter?.isEnabled == true


        override fun onCreate(savedInstanceState: Bundle?) {
            val composeView = findViewById<ComposeView>(R.id.compose_view)
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            // bluetooth elements
            val enableBluetoothLauncher = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { /* Not needed */ }
            val permissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { perms ->
                val canEnableBluetooth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    perms[Manifest.permission.BLUETOOTH_CONNECT] == true
                } else true

                if (canEnableBluetooth && !isBluetoothEnabled) {
                    enableBluetoothLauncher.launch(
                        Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    )
                }
            }
            val bluetoothSettingsButton = findViewById<Button>(R.id.bluetoothSettingsButton)
            bluetoothSettingsButton.setOnClickListener {
                val intent = Intent(this, BluetoothSettingsActivity::class.java)
                startActivity(intent)
            }

            // Get the SensorManager
            val sensorManager = getSystemService(SENSOR_SERVICE) as? SensorManager

            // Handle if SensorManager is null
            if (sensorManager == null) {
                // Om SensorManager inte är tillgänglig, visa ett felmeddelande i en dialog eller Toast
                Toast.makeText(
                    this,
                    "SensorManager is not available on this device.",
                    Toast.LENGTH_LONG
                ).show()
                return
            }

            // Get the list of all sensors
            val sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL)

            // Check if sensors are available
            if (sensorList.isNullOrEmpty()) {
                Toast.makeText(this, "No sensors available on this device.", Toast.LENGTH_LONG)
                    .show()
                return
            }

            // Display sensors in RecyclerView with icons and descriptions
            val sensorsForRecyclerView = sensorList.map { deviceSensor ->
                SensorItem(
                    title = deviceSensor.name,
                    description = "Type: ${deviceSensor.type}",
                    icon = R.drawable.sensor, // Placeholder för ikon
                    type = deviceSensor.type // Skicka med sensorns typ
                )
            }
            val sensorRecyclerView = findViewById<RecyclerView>(R.id.sensorRecyclerView)
            sensorRecyclerView.layoutManager = LinearLayoutManager(this)

// Skapa adaptern och hantera klick för varje item
            sensorRecyclerView.adapter = SensorAdapter(sensorsForRecyclerView) { sensorItem ->
                // Visa realtidsdata när en specifik sensor klickas
                val intent = Intent(this, SensorDetailActivity::class.java)
                intent.putExtra("sensor_name", sensorItem.title)
                intent.putExtra("sensor_type", sensorItem.type)
                startActivity(intent)

            }
        }
    }
}
// the commented UI arguments is for refrance on how the bluetooth is supposed to work
//setContent {
//    BluetoothChatTheme {
//        val viewModel = hiltViewModel<BluetoothViewModel>()
//        val state by viewModel.state.collectAsState()
//
//        LaunchedEffect(key1 = state.errorMessage) {
//            state.errorMessage?.let { message ->
//                Toast.makeText(
//                    applicationContext,
//                    message,
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        }
//
//        LaunchedEffect(key1 = state.isConnected) {
//            if(state.isConnected) {
//                Toast.makeText(
//                    applicationContext,
//                    "You're connected!",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//        }
//
//        Surface(
//            color = MaterialTheme.colors.background
//        ) {
//            when {
//                state.isConnecting -> {
//                    Column(
//                        modifier = Modifier.fillMaxSize(),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        CircularProgressIndicator()
//                        Text(text = "Connecting...")
//                    }
//                }
//                state.isConnected -> {
//                    ChatScreen(
//                        state = state,
//                        onDisconnect = viewModel::disconnectFromDevice,
//                        onSendMessage = viewModel::sendMessage
//                    )
//                }
//                else -> {
//                    DeviceScreen(
//                        state = state,
//                        onStartScan = viewModel::startScan,
//                        onStopScan = viewModel::stopScan,
//                        onDeviceClick = viewModel::connectToDevice,
//                        onStartServer = viewModel::waitForIncomingConnections
//                    )
//                }
//            }
//        }
//    }
//}