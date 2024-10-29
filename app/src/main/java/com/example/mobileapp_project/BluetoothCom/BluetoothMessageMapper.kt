package com.example.mobileapp_project.BluetoothCom

fun String.toBluetoothMessage(isFromLocalUser: Boolean):BluetoothMessage {
    val name = substringBeforeLast("#")
    val message = substringAfter("#")
    return BluetoothMessage(
        message = message,
        sender = name,
        isFromLocalUser = isFromLocalUser
    )
}

fun BluetoothMessage.toByteArray(isFromLocalUser: Boolean):ByteArray{
    return "$sender#$message".encodeToByteArray() // there shouldn't be any hashtags in the name which is a limitation it is recomended that you use a real civ parser like Json

}

