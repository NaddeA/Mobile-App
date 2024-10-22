package com.example.mobileapp_project

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var pressureSensor: Sensor? = null
    private var writer: FileWriter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SensorManager and Sensors
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

        // Initialize FileWriter for logging data
        try {
            val logFile = File(getExternalFilesDir(null), "sensors_${System.currentTimeMillis()}.csv")
            writer = FileWriter(logFile, true)
            writer?.write("Timestamp,Sensor,ValueX,ValueY,ValueZ\n")
        } catch (e: IOException) {
            e.printStackTrace()
        }

        setContent {
            SensorApp()
        }
    }

    // Register listeners when the activity is resumed
    override fun onResume() {
        super.onResume()
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        pressureSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    // Unregister listeners when the activity is paused
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        try {
            writer?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // Handle sensor data
    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
                logData("ACCELEROMETER", timestamp, x, y, z)
            }
            Sensor.TYPE_PRESSURE -> {
                val pressure = event.values[0]
                logData("PRESSURE", timestamp, pressure, 0f, 0f)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    // Function to log data to the CSV file
    private fun logData(sensorType: String, timestamp: String, valueX: Float, valueY: Float, valueZ: Float) {
        try {
            writer?.write("$timestamp,$sensorType,$valueX,$valueY,$valueZ\n")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // UI Composables
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SensorApp() {
        var sensorData by remember { mutableStateOf(emptyList<String>()) }
        var isSensorVisible by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Sensor Info") })
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Button(onClick = { // Fetch and update sensor data when button is clicked
                    isSensorVisible = !isSensorVisible

                    if (isSensorVisible) {
                        sensorData = getSensorInfo()
                    }
                }) {
                    // Shows or hides sensors
                    Text(text = if (isSensorVisible) "Hide Sensors" else "Show Sensors")
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isSensorVisible) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(sensorData) { sensor ->
                            Text(
                                text = sensor,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }

    // Get the list of sensors
    private fun getSensorInfo(): List<String> {
        return try {
            val deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL)
            if (deviceSensors.isNotEmpty()) {
                deviceSensors.map { sensor -> "Sensor: ${sensor.name}" }
            } else {
                listOf("No sensors available on this device")
            }
        } catch (e: Exception) {
            listOf("Error fetching sensors: ${e.message}")
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewSensorApp() {
        SensorApp()
    }
}
