package com.example.mobileapp_project

import android.content.Context
import org.json.JSONObject
import java.io.File
// Assuming this is for the alarms and logs to an extent
// Harry should be able to explain it further
data class ChannelConfig(val name: String, val isActive: Boolean)
data class AlarmConfig(val threshold: Float, val alarmType: String) // Alarm types: "positive", "negative", "both"
data class SamplingConfig(val rate: Int, val mode: String) // Modes could be "FIFO" or "newFile"

data class SetupConfig(
    val channels: List<ChannelConfig>,
    val sampling: SamplingConfig,
    val alarm: AlarmConfig
) {
    fun toJson(): String {
        val json = JSONObject()
        json.put("samplingRate", sampling.rate)
        json.put("mode", sampling.mode)
        val channelsJson = channels.map {
            JSONObject().put("name", it.name).put("isActive", it.isActive)
        }
        json.put("channels", channelsJson)
        json.put("alarmThreshold", alarm.threshold)
        json.put("alarmType", alarm.alarmType)
        return json.toString()
    }

    companion object {
        fun fromJson(jsonString: String): SetupConfig {
            val json = JSONObject(jsonString)
            // Parse the configuration data accordingly
            val sampling = SamplingConfig(json.getInt("samplingRate"), json.getString("mode"))
            val alarm = AlarmConfig(json.getDouble("alarmThreshold").toFloat(), json.getString("alarmType"))
            val channels = json.getJSONArray("channels").let { array ->
                (0 until array.length()).map {
                    val channelJson = array.getJSONObject(it)
                    ChannelConfig(channelJson.getString("name"), channelJson.getBoolean("isActive"))
                }
            }
            return SetupConfig(channels, sampling, alarm)
        }
    }
}

fun saveConfigToFile(context: Context, config: SetupConfig) {
    val file = File(context.filesDir, "SetupConfig.json")
    file.writeText(config.toJson())
}

fun loadConfigFromFile(context: Context): SetupConfig {
    val file = File(context.filesDir, "SetupConfig.json")
    if (!file.exists()) return SetupConfig(emptyList(), SamplingConfig(0, ""), AlarmConfig(0f, ""))
    return SetupConfig.fromJson(file.readText())
}
