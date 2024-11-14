package com.example.mobileapp_project.Bluetooth.data.chat

import com.example.mobileapp_project.Bluetooth.domain.chat.BluetoothMessage

// Converts a string formatted as "name#message" into a BluetoothMessage object.
fun String.toBluetoothMessage(isFromLocalUser: Boolean): BluetoothMessage {
    val name = substringBeforeLast("#") // Extracts the sender's name (before the last "#")
    val message = substringAfter("#") // Extracts the message (after the "#")

    // Creates and returns a BluetoothMessage object
    return BluetoothMessage(
        message = message,
        senderName = name,
        isFromLocalUser = isFromLocalUser
    )

}
// Converts a BluetoothMessage object into a byte array for sending over Bluetooth.
fun BluetoothMessage.toByteArray(): ByteArray {
    return "$senderName#$message".encodeToByteArray()
}