// SensorDetailActivity.kt
package com.example.mobileapp_project

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
<<<<<<< HEAD
import android.widget.TextView
=======
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobileapp_project.ui.theme.MobileAppProjectTheme
>>>>>>> 68186fc618ed8bab6bdcc6ebbe18c118fdf5ee56


<<<<<<< HEAD
class SensorDetailManager(
    private val context: Context,
    private val sensorManager: SensorManager,
    private val sensorDataTextView: TextView,
    private val sensorInfoTextView: TextView
) {
=======
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
>>>>>>> 68186fc618ed8bab6bdcc6ebbe18c118fdf5ee56

    private var sensorEventListener: SensorEventListener? = null

<<<<<<< HEAD
    fun showSensorDetails(sensorType: Int) {
        val sensor = sensorManager.getDefaultSensor(sensorType)

        sensor?.let {
            val info = """
                Name: ${it.name}
                Type: ${it.type}
                Maximum Range: ${it.maximumRange}
                Minimum Detectable Change: ${it.resolution}
                Power Consumption: ${it.power} mA
                Update Frequency: ${it.minDelay} µs
            """.trimIndent()
            sensorInfoTextView.text = info

            // Register a listener to display sensor data
            sensorEventListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    val values = event.values.joinToString(", ")
                    sensorDataTextView.text = "Sensor values: $values"
                }

                override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
            }

            sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_UI)
        } ?: run {
            sensorInfoTextView.text = "No sensor information available"
        }
    }

    fun unregisterSensorListener() {
        sensorEventListener?.let {
            sensorManager.unregisterListener(it)
=======
        val sensorName = intent.getStringExtra("sensor_name") ?: "Unknown Sensor"
        val sensorType = intent.getIntExtra("sensor_type", -1)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(sensorType)

        setContent {
            MobileAppProjectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SensorDetailScreen(sensorName, sensor, sensorManager)
                }
            }
        }
    }
}
@Composable
fun SensorDetailScreen(sensorName: String, sensor: Sensor?, sensorManager: SensorManager) {
    var sensorValues by remember { mutableStateOf("") }

    // Hantera registrering och avregistrering av sensorEventListener med DisposableEffect
    DisposableEffect(sensor) {
        // Skapa sensorEventListener
        val sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                // Uppdatera sensorvärdena när sensorn skickar nya data
                sensorValues = event.values.joinToString(", ")
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }

        // Registrera listener när DisposableEffect skapas
        sensor?.let {
            sensorManager.registerListener(sensorEventListener, it, SensorManager.SENSOR_DELAY_UI)
>>>>>>> 68186fc618ed8bab6bdcc6ebbe18c118fdf5ee56
        }

        // Avregistrera listener när composable tas bort (onDispose)
        onDispose {
            sensorManager.unregisterListener(sensorEventListener)
        }
    }
<<<<<<< HEAD
=======

    // UI-komponenter för att visa sensordata
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Sensor: $sensorName", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Sensor values: $sensorValues", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Visa annan sensordata om sensorn är tillgänglig
        sensor?.let {
            Text(text = "Max Range: ${it.maximumRange}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Resolution: ${it.resolution}", style = MaterialTheme.typography.bodySmall)
            Text(text = "Power: ${it.power} mA", style = MaterialTheme.typography.bodySmall)
        } ?: Text(text = "No sensor data available", style = MaterialTheme.typography.bodySmall)
    }
>>>>>>> 68186fc618ed8bab6bdcc6ebbe18c118fdf5ee56
}

