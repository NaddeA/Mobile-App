package com.example.mobileapp_project

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
<<<<<<< HEAD
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
=======
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobileapp_project.ui.theme.MobileAppProjectTheme
>>>>>>> 68186fc618ed8bab6bdcc6ebbe18c118fdf5ee56

@RequiresApi(Build.VERSION_CODES.S)
class MainActivity : ComponentActivity() {

    private val bluetoothManager by lazy {
<<<<<<< HEAD
        applicationContext.getSystemService(BluetoothManager::class.java)
=======
        getSystemService(BluetoothManager::class.java)
>>>>>>> 68186fc618ed8bab6bdcc6ebbe18c118fdf5ee56
    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }
<<<<<<< HEAD

    private val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    private lateinit var sensorManager: SensorManager
    private lateinit var sensorDataTextView: TextView
    private lateinit var sensorInfoTextView: TextView

    private val enableBluetoothLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { /* Not needed */ }

    // Removed permissionLauncher as it was unused

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Bluetooth settings button
        val bluetoothSettingsButton = findViewById<Button>(R.id.bluetoothSettingsButton)
        bluetoothSettingsButton.setOnClickListener {
            toggleBluetooth()
        }

        // Get the SensorManager
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // Display Sensors in RecyclerView
        displaySensors()

        // Setting up sensor details text view
        sensorDataTextView = findViewById(R.id.sensorDataTextView)
        sensorInfoTextView = findViewById(R.id.sensorInfoTextView)
    }

    private fun toggleBluetooth() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, getString(R.string.bluetooth_not_supported), Toast.LENGTH_LONG).show()
            return
        }

        if (!bluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            enableBluetoothLauncher.launch(enableBtIntent)
        } else {
            Toast.makeText(this, getString(R.string.bluetooth_already_enabled), Toast.LENGTH_SHORT).show()
        }
    }

    private fun displaySensors() {
        val sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL)
        if (sensorList.isNullOrEmpty()) {
            Toast.makeText(this, getString(R.string.no_sensors_available), Toast.LENGTH_LONG).show()
            return
        }

        val sensorsForRecyclerView = sensorList.map { deviceSensor ->
            SensorItem(
                title = deviceSensor.name,
                description = getString(R.string.sensor_type, deviceSensor.type),
                icon = R.drawable.sensor,
                type = deviceSensor.type
            )
        }

        val sensorRecyclerView = findViewById<RecyclerView>(R.id.sensorRecyclerView)
        sensorRecyclerView.layoutManager = LinearLayoutManager(this)
        sensorRecyclerView.adapter = SensorAdapter(sensorsForRecyclerView) { sensorItem ->
            showSensorDetails(sensorItem)
        }
    }

    private fun showSensorDetails(sensorItem: SensorItem) {
        val sensor = sensorManager.getDefaultSensor(sensorItem.type)

        sensor?.let {
            val info = """
                ${getString(R.string.sensor_name)}: ${it.name}
                ${getString(R.string.sensor_type)}: ${it.type}
                ${getString(R.string.sensor_maximum_range)}: ${it.maximumRange}
                ${getString(R.string.sensor_resolution)}: ${it.resolution}
                ${getString(R.string.sensor_power_consumption)}: ${it.power} mA
                ${getString(R.string.sensor_update_frequency)}: ${it.minDelay} µs
            """.trimIndent()
            sensorInfoTextView.text = info

            // Register a listener to display sensor data
            val sensorEventListener = object : SensorEventListener {
                override fun onSensorChanged(event: android.hardware.SensorEvent) {
                    val values = event.values.joinToString(", ")
                    sensorDataTextView.text = getString(R.string.sensor_values, values)
                }

                override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
            }

            // Register listener during activity lifecycle
            sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_UI)

            // Unregister listener appropriately in onPause and onResume
            this.sensorEventListener = sensorEventListener
        } ?: run {
            sensorInfoTextView.text = getString(R.string.no_sensor_information)
        }
    }

    private var sensorEventListener: SensorEventListener? = null

    override fun onPause() {
        super.onPause()
        sensorEventListener?.let {
            sensorManager.unregisterListener(it)
        }
    }

    override fun onResume() {
        super.onResume()
        sensorEventListener?.let {
            val sensor = sensorManager.getDefaultSensor(it.hashCode()) // Should be set to a specific sensor type if needed
            if (sensor != null) {
                sensorManager.registerListener(it, sensor, SensorManager.SENSOR_DELAY_UI)
            }
        }
    }
}
=======

    private val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled == true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // SensorManager instansieras här och skickas till MainScreen
        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensorList = listOf(
            SensorItem(
                title = "Accelerometer",
                description = "Type: 1",
                icon = R.drawable.sensor, // Sätt din standardikon här
                sensorType = Sensor.TYPE_ACCELEROMETER
            ),
            SensorItem(
                title = "Gyroscope",
                description = "Type: 4",
                icon = R.drawable.sensor, // Sätt din standardikon här
                sensorType = Sensor.TYPE_GYROSCOPE
            ),

        )

        setContent {
            MobileAppProjectTheme {
                MainScreen(
                    sensorManager = sensorManager,
                    isBluetoothEnabled = isBluetoothEnabled,
                    onEnableBluetooth = { enableBluetooth() },
                    onSensorClick = { sensorItem ->
                        val intent = Intent(this, SensorDetailActivity::class.java).apply {
                            putExtra("sensor_name", sensorItem.title)
                            putExtra("sensor_type", sensorItem.sensorType)
                        }
                        startActivity(intent)
                    },
                    onBluetoothSettingsClick = {
                        startActivity(Intent(this, BluetoothSettingsActivity::class.java))
                    }
                )
            }
        }
    }

    private fun enableBluetooth() {
        val enableBluetoothLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // Här kan du hantera resultatet om det behövs
        }

        val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val canEnableBluetooth = permissions[Manifest.permission.BLUETOOTH_CONNECT] == true || Build.VERSION.SDK_INT < Build.VERSION_CODES.S
            if (canEnableBluetooth && !isBluetoothEnabled) {
                enableBluetoothLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionLauncher.launch(arrayOf(Manifest.permission.BLUETOOTH_CONNECT))
        } else if (!isBluetoothEnabled) {
            enableBluetoothLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        }
    }
}

// MainScreen composable
@Composable
fun MainScreen(
    sensorManager: SensorManager,
    isBluetoothEnabled: Boolean,
    onEnableBluetooth: () -> Unit,
    onSensorClick: (SensorItem) -> Unit,
    onBluetoothSettingsClick: () -> Unit
) {
    var sensorList by remember { mutableStateOf<List<SensorItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        sensorList = getSensorList(sensorManager)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onBluetoothSettingsClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Bluetooth Settings")
        }

        Text(
            "Available Sensors:",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        SensorList(sensorList, onSensorClick)
    }
}

// Flytta sensordatahämtningen till en separat funktion
fun getSensorList(sensorManager: SensorManager): List<SensorItem> {
    return sensorManager.getSensorList(Sensor.TYPE_ALL).map { deviceSensor ->
        SensorItem(
            title = deviceSensor.name,
            description = "Type: ${deviceSensor.type}",icon = android.R.drawable.ic_menu_info_details
            , // Placeholder icon
            sensorType = deviceSensor.type
        )
    }
}
>>>>>>> 68186fc618ed8bab6bdcc6ebbe18c118fdf5ee56
