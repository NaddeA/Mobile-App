package com.example.mobileapp_project.slave

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap


data class ChannelConfig(
    val channelId: Int,
    val sensorType: Int,
    val channelName: String = "Channel $channelId",
    var triggerLevel: Float? = null,
    var alarmLevel: Float? = null,
    var isEnabled: Boolean = false // whether the channel is active
)

class AppSensorManager(
    private val context: Context,
    private val onSensorDataCollected: (String, Float, Float, Float) -> Unit
) : SensorEventListener {

    private val channelConfigs = mutableListOf<ChannelConfig>()
    private val sensorDataList = mutableListOf<String>()

    fun setupChannels(configs: List<ChannelConfig>) {
        channelConfigs.clear()
        channelConfigs.addAll(configs)
    }

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val channelData = ConcurrentHashMap<Int, Float>() // Map to store data per channel

    // Assign channels to each sensor component
    private val sensorChannels = mapOf(
        Sensor.TYPE_ACCELEROMETER to listOf(1, 2, 3),        // X, Y, Z channels for accelerometer
        Sensor.TYPE_GYROSCOPE to listOf(4, 5, 6),            // X, Y, Z channels for gyroscope
        Sensor.TYPE_MAGNETIC_FIELD to listOf(7, 8, 9),       // X, Y, Z channels for magnetic field
        Sensor.TYPE_LIGHT to listOf(10),                     // Light intensity channel
        Sensor.TYPE_PROXIMITY to listOf(11),                 // Proximity channel
        Sensor.TYPE_PRESSURE to listOf(12),                  // Pressure channel
        Sensor.TYPE_AMBIENT_TEMPERATURE to listOf(13),       // Temperature channel
        Sensor.TYPE_RELATIVE_HUMIDITY to listOf(14),         // Humidity channel
        Sensor.TYPE_ROTATION_VECTOR to listOf(15, 16, 17),   // X, Y, Z channels for rotation vector
        // Add more channels if needed, up to 32
    )

    private var bufferedWriter: BufferedWriter? = null
    private var isCollectingData = false
    private var rowCount = 0
    private val maxRows = 1000
    private val logFileName = "Logdata.txt"

    init {
        initializeLogFile()
    }

    private fun initializeLogFile() {
        try {
            val logFile = File(context.getExternalFilesDir(null), logFileName)
            bufferedWriter = BufferedWriter(FileWriter(logFile, true))

            // Write header if the file is empty
            if (logFile.length() == 0L) {
                val header = String.format(
                    "%-20s %-12s %-12s %-18s %-18s %-18s %-12s %-12s %-12s %-12s %-18s %-18s %-18s\n",
                    "Date/Time", "Pressure", "Temperature", "Accelerometer X", "Accelerometer Y", "Accelerometer Z",
                    "Humidity", "Light", "Proximity", "Gyroscope X", "Gyroscope Y", "Gyroscope Z"
                )
                bufferedWriter?.write(header)
                bufferedWriter?.flush()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun startDataCollection() {
        if (!isCollectingData) {
            isCollectingData = true
            sensorChannels.keys.forEach { sensorType ->
                sensorManager.getDefaultSensor(sensorType)?.let {
                    sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
                }
            }
        }
    }


    fun stopDataCollection() {
        if (isCollectingData) {
            isCollectingData = false
            sensorManager.unregisterListener(this)
            closeLogFile()
        }
    }

    // Logs sensor data and updates channelData map
    private fun logSensorData(values: Map<Int, Float?>) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS", Locale.getDefault()).format(Date())
        val logLine = String.format(
            "%-20s %-12s %-12s %-18s %-18s %-18s %-12s %-12s %-12s %-12s %-18s %-18s %-18s\n",
            timestamp,
            formatValue(values[12]),      // Pressure
            formatValue(values[13]),      // Temperature
            formatValue(values[1]),       // Accelerometer X
            formatValue(values[2]),       // Accelerometer Y
            formatValue(values[3]),       // Accelerometer Z
            formatValue(values[14]),      // Humidity
            formatValue(values[10]),      // Light
            formatValue(values[11]),      // Proximity
            formatValue(values[4]),       // Gyroscope X
            formatValue(values[5]),       // Gyroscope Y
            formatValue(values[6])        // Gyroscope Z
        )

        try {
            bufferedWriter?.apply {
                write(logLine)
                flush()
            }
            rowCount++
            if (rowCount >= maxRows) {
                rotateLogFile()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun rotateLogFile() {
        try {
            bufferedWriter?.close()
            val timestamp = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(Date())
            val oldLogFile = File(context.getExternalFilesDir(null), logFileName)
            val newLogFile = File(context.getExternalFilesDir(null), "Logdata_$timestamp.txt")
            oldLogFile.renameTo(newLogFile)
            initializeLogFile()
            rowCount = 0
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun closeLogFile() {
        try {
            bufferedWriter?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (!isCollectingData || event == null) return

        val channels = sensorChannels[event.sensor.type] ?: return
        val values = event.values
        val channelValues = mutableMapOf<Int, Float>()

        // Map sensor values to channels
        for (i in channels.indices) {
            if (i < values.size) {
                channelData[channels[i]] = values[i]
                channelValues[channels[i]] = values[i]
            }
        }

        logSensorData(channelValues)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Optional: Handle accuracy changes if necessary
    }

    private fun formatValue(value: Float?): String {
        return if (value == null) "" else String.format("%.6f", value)
    }

    // Method to retrieve data from a specific channel
    fun getChannelData(channel: Int): Float? {
        return channelData[channel]
    }
    fun getLoggedData(): String {
        // Combine all sensor data into a single string, or return as needed
        return sensorDataList.joinToString("\n")
    }
}
