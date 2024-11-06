package com.example.mobileapp_project.Bluetooth.domain.chat

import com.example.mobileapp_project.Bluetooth.domain.chat.BluetoothMessage

// Sealed interface representing the possible results of a Bluetooth connection operation.
sealed interface ConnectionResult {
    // Connection was successful?>
    object ConnectionEstablished: ConnectionResult

    // transfer was successful ?>
    data class TransferSucceeded(val message: BluetoothMessage): ConnectionResult

    // Yeah you guessed right errors
    data class Error(val message: String): ConnectionResult
}