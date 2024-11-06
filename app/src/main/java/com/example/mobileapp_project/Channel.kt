package com.example.mobileapp_project

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Channel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "Channel",
    val sensorType: String = "Dummy",  //  Sensor type, e.g., Accelerometer, Gyroscope
    val triggerLevel: Float = 0.0f,    // Trigger level for alarms
    val alarmType: String = "None",    // Alarm type, e.g., Beep, SMS
    val isActivated: Boolean = false   // Channel activation status
)