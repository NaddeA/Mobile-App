package com.example.mobileapp_project
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

class AppSensorManager(
    private val context: Context,
    private val onSensorDataCollected: (String, Float, Float, Float) -> Unit
) : SensorEventListener {

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    // Define sensors
    private var accelerometer: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var gyroscope: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    private var magneticField: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    private var lightSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    private var proximitySensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
    private var pressureSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
    private var temperatureSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
    private var humiditySensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
    private var rotationVector: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

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
                    "Date/Time", "Pressure 1", "Pressure 2", "Accelerometer X", "Accelerometer Y", "Accelerometer Z",
                    "Temperature", "Humidity", "Light", "Proximity", "Gyroscope X", "Gyroscope Y", "Gyroscope Z"
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
            accelerometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
            gyroscope?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
            magneticField?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
            lightSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
            proximitySensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
            pressureSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
            temperatureSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
            humiditySensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
            rotationVector?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
        }
    }

    fun stopDataCollection() {
        if (isCollectingData) {
            isCollectingData = false
            sensorManager.unregisterListener(this)
            closeLogFile()
        }
    }

    private fun logSensorData(values: Map<String, Float?>) {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS", Locale.getDefault()).format(Date())

        // Construct the formatted line with consistent column widths
        val logLine = String.format(
            "%-20s %-12s %-12s %-18s %-18s %-18s %-12s %-12s %-12s %-12s %-18s %-18s %-18s\n",
            timestamp,
            formatValue(values["Pressure 1"]),
            formatValue(values["Pressure 2"]),
            formatValue(values["Accelerometer X"]),
            formatValue(values["Accelerometer Y"]),
            formatValue(values["Accelerometer Z"]),
            formatValue(values["Temperature"]),
            formatValue(values["Humidity"]),
            formatValue(values["Light"]),
            formatValue(values["Proximity"]),
            formatValue(values["Gyroscope X"]),
            formatValue(values["Gyroscope Y"]),
            formatValue(values["Gyroscope Z"])
        )

        try {
            bufferedWriter?.apply {
                write(logLine)
                flush()  // Flush immediately for faster visibility in file
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

        val sensorValues = when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> mapOf(
                "Accelerometer X" to event.values[0],
                "Accelerometer Y" to event.values[1],
                "Accelerometer Z" to event.values[2]
            )
            Sensor.TYPE_GYROSCOPE -> mapOf(
                "Gyroscope X" to event.values[0],
                "Gyroscope Y" to event.values[1],
                "Gyroscope Z" to event.values[2]
            )
            Sensor.TYPE_MAGNETIC_FIELD -> mapOf(
                "Magnetic Field X" to event.values[0],
                "Magnetic Field Y" to event.values[1],
                "Magnetic Field Z" to event.values[2]
            )
            Sensor.TYPE_LIGHT -> mapOf("Light" to event.values[0])
            Sensor.TYPE_PROXIMITY -> mapOf("Proximity" to event.values[0])
            Sensor.TYPE_PRESSURE -> mapOf("Pressure 1" to event.values[0], "Pressure 2" to null)
            Sensor.TYPE_AMBIENT_TEMPERATURE -> mapOf("Temperature" to event.values[0])
            Sensor.TYPE_RELATIVE_HUMIDITY -> mapOf("Humidity" to event.values[0])
            Sensor.TYPE_ROTATION_VECTOR -> mapOf(
                "Rotation Vector X" to event.values[0],
                "Rotation Vector Y" to event.values[1],
                "Rotation Vector Z" to if (event.values.size > 2) event.values[2] else null
            )
            else -> emptyMap()
        }

        // Log data if there are any values to write
        if (sensorValues.isNotEmpty()) {
            logSensorData(sensorValues)
            onSensorDataCollected(
                event.sensor.name,
                sensorValues["Accelerometer X"] ?: 0f,
                sensorValues["Accelerometer Y"] ?: 0f,
                sensorValues["Accelerometer Z"] ?: 0f
            )
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Optional: Handle accuracy changes if necessary
    }

    private fun formatValue(value: Float?): String {
        return if (value == null) "" else String.format("%.6f", value)
    }
}
