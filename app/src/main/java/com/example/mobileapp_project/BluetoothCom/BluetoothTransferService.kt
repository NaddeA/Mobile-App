package com.example.mobileapp_project.BluetoothCom

import android.bluetooth.BluetoothSocket

class BluetoothTransferService (
    private val  socket: BluetoothSocket
){
    fun listenForIncomingMessages():Flow(ConnectionResult) // this is logic taken from @PhilippLackner on Youtube I will work on and identify necessary changes later on
}