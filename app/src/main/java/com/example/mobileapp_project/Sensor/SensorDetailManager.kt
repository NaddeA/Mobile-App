package com.example.mobileapp_project.Sensor


//This is being called or initiated in main as sensorManager
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.mobileapp_project.LogDataService

class SensorDetailManager(private val context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var sensorEventListener: SensorEventListener? = null
    private val logDataService = LogDataService(context) // Initialize the LogDataService

    // Register the sensor and log data
    fun registerSensor(sensorType: Int, onSensorDataUpdate: (String) -> Unit) {
        val sensor = sensorManager.getDefaultSensor(sensorType)
        sensor?.let {
            sensorEventListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    val values = event.values.joinToString(", ")
                    onSensorDataUpdate(values)
                    logDataService.logSensorData(sensor.name, values) // Log sensor data whenever it changes
                }

                override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
            }
            sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    // Unregister sensor to stop listening
    fun unregisterSensor() {
        sensorEventListener?.let {
            sensorManager.unregisterListener(it)
        }
    }
}
