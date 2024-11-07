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
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : ComponentActivity() {

    private val bluetoothManager by lazy {
        applicationContext.getSystemService(BluetoothManager::class.java)
    }
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

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
