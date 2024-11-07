package com.example.mobileapp_project.Bluetooth.presentation

import com.example.mobileapp_project.Bluetooth.domain.chat.BluetoothDevice
import com.example.mobileapp_project.Bluetooth.domain.chat.BluetoothMessage

data class BluetoothUiState(
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevices: List<BluetoothDevice> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
    val messages: List<BluetoothMessage> = emptyList()
)
