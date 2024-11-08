package com.example.mobileapp_project

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileappproject.R

class BluetoothSettingsActivity : ComponentActivity() {

    private val bluetoothAdapter: BluetoothAdapter? by lazy { BluetoothAdapter.getDefaultAdapter() }
    private lateinit var enableBluetoothLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_settings)

        // Check if the device supports Bluetooth
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_LONG).show()
        }

        // Initialize the ActivityResultLauncher to enable Bluetooth
        enableBluetoothLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Bluetooth enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Bluetooth enabling was cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to handle the click event for enabling Bluetooth
    fun onEnableBluetoothClicked(view: View) {
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter!!.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                enableBluetoothLauncher.launch(enableBtIntent)
            } else {
                Toast.makeText(this, "Bluetooth is already enabled", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_LONG).show()
        }
    }

    // Function to handle the click event for Master Mode
    fun onMasterModeClicked(view: View) {
        // Logic to set device in Master Mode (Placeholder implementation)
        Toast.makeText(this, "Master mode activated", Toast.LENGTH_SHORT).show()
    }

    // Function to handle the click event for Slave Mode
    fun onSlaveModeClicked(view: View) {
        // Logic to set device in Slave Mode (Placeholder implementation)
        Toast.makeText(this, "Slave mode activated", Toast.LENGTH_SHORT).show()
    }
}
