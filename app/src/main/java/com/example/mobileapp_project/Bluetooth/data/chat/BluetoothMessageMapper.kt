package com.example.mobileapp_project.Bluetooth.data.chat

import android.bluetooth.BluetoothSocket
import com.example.mobileapp_project.Bluetooth.domain.chat.BluetoothMessage

// Converts a string formatted as "name#message" into a BluetoothMessage object.
fun String.toBluetoothMessage(isFromLocalUser: Boolean,socket:BluetoothSocket): BluetoothMessage {
    val name = substringBeforeLast("#") // Extracts the sender's name (before the last "#")
    val message = substringAfter("#") // Extracts the message (after the "#")

    if (message[0] == '['){

        // here you call to change what it does before returning the message
        return BluetoothMessage(
            message = "Command registerd "+ message,
            senderName = name,
            isFromLocalUser = isFromLocalUser
        )

    }

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