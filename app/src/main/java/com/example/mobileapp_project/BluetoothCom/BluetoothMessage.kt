package com.example.mobileapp_project.BluetoothCom

data class BluetoothMessage (
    val message:String,
    val sender : String,
    val isFromLocalUser: Boolean = false // We need to know if we sent this or received it
)
