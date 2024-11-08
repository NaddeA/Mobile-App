package com.example.mobileapp_project

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobileappproject.R

class SensorDetailActivity : ComponentActivity() {

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
    private lateinit var sensorEventListener: SensorEventListener
    private lateinit var sensorDataTextView: TextView
    private lateinit var sensorInfoTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the layout for XML views
        setContentView(R.layout.activity_sensor_detail)

        // Find XML views (keeping your original structure)
        sensorDataTextView = findViewById(R.id.sensorDataTextView)
        sensorInfoTextView = findViewById(R.id.sensorInfoTextView)

        // Get sensor information from Intent
        val sensorName = intent.getStringExtra("sensor_name") ?: "Unknown Sensor"
        val sensorType = intent.getIntExtra("sensor_type", -1)

        // Set the title to the sensor's name
        title = sensorName

        // Initialize SensorManager and sensor
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(sensorType)

        // Display sensor information in XML view
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
        } ?: run {
            sensorInfoTextView.text = "No sensor information available"
        }

        // Set up a SensorEventListener to display real-time data in XML view
        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val values = event.values.joinToString(", ")
                sensorDataTextView.text = "Sensor values: $values"
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
        }

        // Load the Compose content alongside the XML layout
        setContent {
            SensorDetailScreen(sensorName = sensorName, sensorType = sensorType.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        sensor?.also {
            sensorManager.registerListener(sensorEventListener, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorEventListener)
    }
}

@Composable
fun SensorDetailScreen(sensorName: String, sensorType: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Sensor: $sensorName")
        Text(text = "Type: $sensorType")
    }
}
