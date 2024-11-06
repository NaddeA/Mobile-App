package com.example.mobileapp_project.models

data class ChannelConf(
    val channelId: Int,
    var sensorType: Int,
    var channelName: String = "Channel $channelId",
    var triggerLevel: Float? = null,
    var alarmLevel: Float? = null,
    var isEnabled: Boolean = false // whether the channel is active
)