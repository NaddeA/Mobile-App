package com.example.mobileapp_project

import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the SensorManager
        val sensorManager = getSystemService(SENSOR_SERVICE) as? SensorManager

        // Handle if SensorManager is null
        if (sensorManager == null) {
            val textView = findViewById<TextView>(R.id.sensorTextView)
            textView?.text = "SensorManager is not available on this device."
            return
        }

        // Get the list of all sensors
        val sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL)

        // Check if sensors are available
        if (sensorList.isNullOrEmpty()) {
            val textView = findViewById<TextView>(R.id.sensorTextView)
            textView?.text = "No sensors available on this device."
            return
        }

        // Display sensors in TextView as a simple list
        val sensorText = StringBuilder("Available Sensors:\n")
        for (sensor in sensorList) {
            sensorText.append("${sensor.name}\n")
        }
        val textView = findViewById<TextView>(R.id.sensorTextView)
        textView?.text = sensorText.toString()

        // Display sensors in RecyclerView with icons and descriptions
        val sensorsForRecyclerView = sensorList.map { deviceSensor ->
            SensorItem(
                title = deviceSensor.name,
                description = "Type: ${deviceSensor.type}",
                icon = R.drawable.sensor // Replace with an actual icon resource if available
            )
        }

        // Set up RecyclerView with SensorAdapter
        val sensorRecyclerView = findViewById<RecyclerView>(R.id.sensorRecyclerView)
        sensorRecyclerView.layoutManager = LinearLayoutManager(this)
        sensorRecyclerView.adapter = SensorAdapter(sensorsForRecyclerView)
    }
}
