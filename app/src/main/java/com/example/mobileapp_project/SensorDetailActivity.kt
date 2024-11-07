package com.example.mobileapp_project

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SensorDetailActivity : AppCompatActivity() {

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
    private lateinit var sensorEventListener: SensorEventListener
    private lateinit var sensorDataTextView: TextView
    private lateinit var sensorInfoTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sensor_detail)

        // Get sensor information from Intent
        val sensorName = intent.getStringExtra("sensor_name") ?: "Unknown Sensor"
        val sensorType = intent.getIntExtra("sensor_type", -1)

        // Set the title to the sensor's name
        title = sensorName

        // Find TextView to display sensor values
        sensorDataTextView = findViewById(R.id.sensorDataTextView)
        sensorInfoTextView = findViewById(R.id.sensorInfoTextView) // New TextView for sensor information

        // Initialize SensorManager and sensor
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(sensorType)

        // Display sensor information
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

        // Set up a SensorEventListener to display real-time data
        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val values = event.values.joinToString(", ")
                sensorDataTextView.text = "Sensor values: $values"
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
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
