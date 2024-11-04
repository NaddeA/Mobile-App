package com.example.mobileapp_project.sensorHandler

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class SensorManagerHelper(private val context: Context) : SensorEventListener {

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val logFile = File(context.getExternalFilesDir(null), "Logdata.txt")

    init {
        initializeLogFile()
    }

    private fun initializeLogFile() {
        if (!logFile.exists()) {
            try {
                logFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    fun getLoggedData(): String {
        return try {
            logFile.readText()
        } catch (e: IOException) {
            "Error reading log data"
        }
    }


    fun startDataCollection(sensorType: Int) {
        val sensor = sensorManager.getDefaultSensor(sensorType)
        sensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stopDataCollection() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val data = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS", Locale.getDefault()).format(Date())}, ${it.values.joinToString(", ")}\n"
            try {
                FileWriter(logFile, true).use { writer ->
                    writer.append(data)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
