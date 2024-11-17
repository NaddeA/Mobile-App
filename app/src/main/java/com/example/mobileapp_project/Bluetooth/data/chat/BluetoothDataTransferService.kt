package com.example.mobileapp_project.Bluetooth.data.chat

import android.bluetooth.BluetoothSocket
import com.example.mobileapp_project.Bluetooth.domain.chat.BluetoothMessage
import com.example.mobileapp_project.Bluetooth.domain.chat.TransferFailedException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.IOException

class BluetoothDataTransferService(
    private val socket: BluetoothSocket // Bluetooth socket for sending and receiving data
) {
    // Listens for incoming messages from the connected device
    fun listenForIncomingMessages(): Flow<BluetoothMessage> {
        return flow {
            if (!socket.isConnected) {
                return@flow // Stop if socket is not connected
            }

            val buffer = ByteArray(2048) // Buffer to store incoming data
            while (true) {
                // Try reading data from the input stream
                val byteCount = try {
                    socket.inputStream.read(buffer)
                } catch (e: IOException) {
                    throw TransferFailedException() // If read fails, throw exception
                }

                // Emit the decoded message
                emit(
                    buffer.decodeToString(endIndex = byteCount) // Convert bytes to string
                        .toBluetoothMessage(isFromLocalUser = false,socket) // Create BluetoothMessage
                )
            }
        }.flowOn(Dispatchers.IO) // Run on IO thread to avoid blocking UI
    }

    // Sends a message to the connected device
    suspend fun sendMessage(bytes: ByteArray): Boolean {
        return withContext(Dispatchers.IO) { // Use IO thread for network operations
            try {
                socket.outputStream.write(bytes) // Write bytes to output stream
            } catch (e: IOException) {
                e.printStackTrace() // Log error if sending fails
                return@withContext false // Return false if sending fails
            }

            true // Return true if sending is successful
        }
    }
}
