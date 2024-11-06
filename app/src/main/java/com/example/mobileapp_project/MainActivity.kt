package com.example.mobileapp_project

import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
            // Om SensorManager inte är tillgänglig, visa ett felmeddelande i en dialog eller Toast
            Toast.makeText(this, "SensorManager is not available on this device.", Toast.LENGTH_LONG).show()
            return
        }

        // Get the list of all sensors
        val sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL)

        // Check if sensors are available
        if (sensorList.isNullOrEmpty()) {
            Toast.makeText(this, "No sensors available on this device.", Toast.LENGTH_LONG).show()
            return
        }

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


