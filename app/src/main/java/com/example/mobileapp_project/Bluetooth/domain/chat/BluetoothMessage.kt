package com.example.mobileapp_project.Bluetooth.domain.chat

data class BluetoothMessage(
    val message: String,
    val senderName: String,
    val isFromLocalUser: Boolean
)
