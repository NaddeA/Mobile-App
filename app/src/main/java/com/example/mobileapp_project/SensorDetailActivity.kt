package com.example.mobileapp_project

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.TextView


class SensorDetailManager(
    private val context: Context,
    private val sensorManager: SensorManager,
    private val sensorDataTextView: TextView,
    private val sensorInfoTextView: TextView
) {

    private var sensorEventListener: SensorEventListener? = null

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
        }
    }
}
