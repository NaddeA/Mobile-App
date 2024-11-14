package com.example.mobileapp_project


// this is to log the data in a specific way
import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class LogDataService(private val context: Context) {
    private val logFile = File(context.filesDir, "LogData.txt")

    // Log arbitrary data with a timestamp
    fun logData(data: String) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        logFile.appendText("$timestamp: $data\n")
    }

    // Log sensor data
    fun logSensorData(sensorName: String, value: String) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        logFile.appendText("$timestamp - Sensor: $sensorName, Value: $value\n")
    }

    // Read the entire log

    fun readLogData(): List<String> {
        if (!logFile.exists()) return emptyList()
        return logFile.readLines()
    }
}
