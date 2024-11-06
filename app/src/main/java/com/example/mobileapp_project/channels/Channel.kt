package com.example.mobileapp_project.channels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Channel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "Channel",
    val sensorType: String = "Dummy",
    val triggerLevel: Float = 0.0f,
    val alarmType: String = "None",
    val isActivated: Boolean = false
)
