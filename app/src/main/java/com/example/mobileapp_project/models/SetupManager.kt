// SetupManager.kt
package com.example.mobileapp_project.models

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class SetupManager(private val context: Context) {
    private val channels = List(32) { ChannelConf(it + 1, 0) } // Adjust default sensorType if needed
    var samplingRate: Int = 1000
    var startTime: Long? = null
    var stopTime: Long? = null
    var useSlaveTimestamp: Boolean = false
    var logEnabled: Boolean = true
    var maxLogRows: Int = 1000
    var useFifo: Boolean = true

    private val scope = CoroutineScope(Dispatchers.IO)

    fun saveSetup() {
        scope.launch {
            val setupFile = File(context.getExternalFilesDir(null), "setup.txt")
            setupFile.writeText(createSetupString())
        }
    }

    fun loadSetup() {
        scope.launch {
            val setupFile = File(context.getExternalFilesDir(null), "setup.txt")
            if (setupFile.exists()) {
                parseSetupFile(setupFile.readText())
            }
        }
    }

    private fun createSetupString(): String {
        return channels.joinToString("\n") { channel ->
            "${channel.channelId},${channel.channelName},${channel.isEnabled},${channel.sensorType},${channel.triggerLevel}"
        }
    }

    private fun parseSetupFile(data: String) {
        data.lines().forEach { line ->
            val parts = line.split(",")
            if (parts.size >= 5) {
                val id = parts[0].toInt()
                channels.find { it.channelId == id }?.apply {
                    channelName = parts[1]
                    isEnabled = parts[2].toBoolean()
                    sensorType = parts[3].toIntOrNull() ?: 0
                    triggerLevel = parts[4].toFloatOrNull()
                }
            }
        }
    }
}
