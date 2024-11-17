package com.example.mobileapp_project.Bluetooth.CMD

import android.bluetooth.BluetoothSocket
import android.content.Context
import android.hardware.SensorManager
import android.hardware.SensorPrivacyManager
import android.widget.Toast
import com.example.mobileapp_project.Bluetooth.data.chat.BluetoothDataTransferService
import com.example.mobileapp_project.Sensor.SensorDetailManager
import com.example.mobileapp_project.Sensor.SensorItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommandCenter(
    private val context: Context,  // Include context as a class property
    private val command: String = "",
    private var sampleNumber: Int = 10,
    private val sensorList: List<SensorItem>,
    private val socket: BluetoothSocket

) {
    private val bluetoothService:BluetoothDataTransferService = BluetoothDataTransferService(socket)
    private val sensorManager: SensorDetailManager = SensorDetailManager(context)
    private var argument: Int? = command.split(',')[1].toIntOrNull()

    // Centralized method to process commands without external parameters
    fun processCommand(): Any {
        if (argument == null) {
            val argumentMessage = "Correct Argument: [<InsertCommandLetter[e]>,<insertSensorIndex>]"
            Toast.makeText(context, "Invalid argument: $argumentMessage", Toast.LENGTH_SHORT).show()
            return "Invalid argument"
        }
        return when (command[0].lowercase()) {
            "a" -> setSamplingRate()
            "b" -> setNumberOfSamples()
            "c" -> setSamplingConfiguration()
            "d" -> sampleAndTransferData()
            "e" -> sendSampledData { data ->
                val messageToSend = "Collected Sensor Data:\n$data"
                sendMessage(messageToSend)
            }
            "f" -> setClockAndDate()
            "g" -> showDeviceStatus()
            "h" -> setWaitTime()
            "i" -> displayTime()
            "o" -> setActiveChannels()
            "p" -> showMode()
            "q" -> streaming()
            "v" -> showVersion()
            "s" -> stopSampling()
            else -> "Unknown command"
        }
    }

    private fun sendSampledData(onDataCollected: (String) -> Unit) {
        val collectedData = mutableListOf<String>()
        var sampleCount = 0

        sensorManager.registerSensor(sensorList[argument!!].sensorType) { sensordata ->
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val sensorName = sensorList[argument!!].title
            val formattedData = "$timestamp - Sensor: $sensorName, Value: $sensordata"
            collectedData.add(formattedData)

            sampleCount++

            if (sampleCount >= sampleNumber) {
                val result = collectedData.joinToString("\n")
                onDataCollected(result)
            }
        }
    }

    private fun sendMessage(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                bluetoothService.sendMessage(message.toByteArray())
            } catch (e: Exception) {
                println("Failed to send message: ${e.message}")
                // Handle the exception (logging, retrying, user feedback, etc.)

            }
        }
        println("Sending message: $message")
    }

    // Other command methods...



    private fun setSamplingRate(): String {
        // Implement logic for setting the sampling rate
        return "Set sampling rate"
    }

    private fun setNumberOfSamples(): String {
        // Implement logic for setting the number of samples
        return "Set number of samples"
    }

    private fun setSamplingConfiguration(): String {
        // Implement logic for setting the sampling configuration
        return "Set sampling configuration"
    }

    private fun sampleAndTransferData(): String {
        // Implement logic for sampling and transferring data
        return "Sampled and transferred data"
    }

//    private fun sendSampledData(
//    onDataCollected:(String)-> Unit) {
//        val collectedData = mutableListOf<String>()
//        var sampleCount = 0
//
//        sensorManager.registerSensor(sensorList[argument!!].sensorType){sensordata ->
//            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
//            val sensorName = sensorList[argument!!].title
//            val formattedData = "$timestamp - Sensor: $sensorName, Value: $sensordata"
//            collectedData.add(formattedData)
//
//            sampleCount++
//
//            // Stop collecting data after sampleNumber samples
//            if (sampleCount >= sampleNumber) {
//                // At this point, you have enough samples, return the concatenated data
//                val result = collectedData.joinToString("\n")
//                onDataCollected(result)
//            }
//        }
//    }

    private fun setClockAndDate(): String {
        // Implement logic for setting the clock and date
        return "Set clock and date"
    }

    private fun showDeviceStatus(): String {
        // Implement logic for showing the device status
        return "Device status shown"
    }

    private fun setWaitTime(): String {
        // Implement logic for setting wait time
        return "Set wait time between sampling periods"
    }

    private fun displayTime(): String {
        // Implement logic for displaying time
        return "Displayed time and date"
    }

    private fun setActiveChannels(): String {
        // Implement logic for setting active channels
        return "Set active channels"
    }

    private fun showMode(): String {
        // Implement logic for showing the mode
        return "Mode shown"
    }

    private fun streaming(): String {
        // Implement logic for streaming
        return "Started streaming"
    }

    private fun showVersion(): String {
        // Implement logic for showing version
        return "Version shown"
    }

    private fun stopSampling(): String {
        // Implement logic for stopping sampling
        return "Stopped sampling"
    }
}
