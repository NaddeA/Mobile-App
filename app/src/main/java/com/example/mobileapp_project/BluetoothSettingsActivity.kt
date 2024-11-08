// BluetoothSettingsActivity.kt
package com.example.mobileapp_project

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mobileapp_project.ui.theme.MobileAppProjectTheme

class BluetoothSettingsActivity : ComponentActivity() {

    private val bluetoothAdapter: BluetoothAdapter? by lazy { BluetoothAdapter.getDefaultAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileAppProjectTheme {
                BluetoothSettingsScreen(
                    onEnableBluetooth = { enableBluetooth() },
                    onMasterMode = { activateMasterMode() },
                    onSlaveMode = { activateSlaveMode() }
                )
            }
        }
    }

    private fun enableBluetooth() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_LONG).show()
            return
        }

        if (!bluetoothAdapter!!.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "Bluetooth enabled", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Bluetooth enabling was cancelled", Toast.LENGTH_SHORT).show()
                }
            }
            launcher.launch(enableBtIntent)
        } else {
            Toast.makeText(this, "Bluetooth is already enabled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun activateMasterMode() {
        Toast.makeText(this, "Master mode activated", Toast.LENGTH_SHORT).show()
    }

    private fun activateSlaveMode() {
        Toast.makeText(this, "Slave mode activated", Toast.LENGTH_SHORT).show()
    }
}
