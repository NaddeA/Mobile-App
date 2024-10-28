package com.example.mobileapp_project.Master

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService


class Bluetooth(private val context: Context) {

    // Package Manager creating an instance to use
    val packageManager= context.packageManager

    //Creating a Bluetooth manager and from it an adapter
    val bluetoothManager : BluetoothManager = context.getSystemService(BluetoothManager::class.java)
    val bluetoothAdapter : BluetoothAdapter? = bluetoothManager.getAdapter()


    // Checking if normal bluetooth is available ( Mostly for premative sensors and older phones), then Low battery bluetooth
    // Checking if it is enabled
    val bluetoothLEAvailable : Boolean
            get() = packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
    val bluetoothAvailable: Boolean
        get() =  packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)
    val bluetoothEnabled : Boolean
        get() = bluetoothAdapter?.isEnabled() == true

    val bluetooth = Bluetooth(context)


    // Method to check if Bluetooth permission is granted
    private fun isBluetoothPermissionGranted(): Boolean {
        val bluetoothPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.BLUETOOTH
        )
        val bluetoothAdminPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.BLUETOOTH_ADMIN
        )

        return bluetoothPermission == PackageManager.PERMISSION_GRANTED &&
                bluetoothAdminPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun isBluetoothAvailable():Boolean {
        if (!bluetoothAvailable || !bluetoothLEAvailable || !bluetoothEnabled || !isBluetoothPermissionGranted()) {
            Log.e("BluetoothHelper", "Bluetooth not supported or enabled.")
            return false
        }
        return true
    }

    @SuppressLint("MissingPermission") // suppressing permission because I already checked for in the //isBluetoothPermissionGranted\\ Method
    fun connectToDevice(deviceName: String) {
        if (!isBluetoothAvailable() ) {
            Log.e("BluetoothHelper", "Bluetooth not supported or enabled.")
            return
        }
        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices

    }


}






//val bluetoothManager = ContextCompat.getSystemService(this,Context.BLUETOOTH_SERVICE) as? BluetoothManager
//val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.getAdapter()
