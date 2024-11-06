// AppNavigator.kt
package com.example.mobileapp_project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.mobileapp_project.bluetooth.BluetoothHelper
import com.example.mobileapp_project.ui.screens.MasterScreen
import com.example.mobileapp_project.ui.screens.ModeSelectionScreen
import com.example.mobileapp_project.ui.screens.SlaveScreen
import com.example.mobileapp_project.ui.screens.BluetoothConnectionScreen

@Composable
fun AppNavigator(bluetoothHelper: BluetoothHelper) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.ModeSelection) }

    when (currentScreen) {
        Screen.ModeSelection -> ModeSelectionScreen { selectedMode ->
            currentScreen = if (selectedMode == Mode.Master) Screen.BluetoothConnection else Screen.SlaveScreen
        }
        Screen.BluetoothConnection -> BluetoothConnectionScreen(
            bluetoothHelper = bluetoothHelper,
            onDeviceConnected = { currentScreen = Screen.MasterScreen },
            onBack = { currentScreen = Screen.ModeSelection }
        )
        Screen.MasterScreen -> MasterScreen(
            bluetoothHelper = bluetoothHelper,
            onBack = { currentScreen = Screen.BluetoothConnection }
        )
        Screen.SlaveScreen -> SlaveScreen(
            bluetoothHelper = bluetoothHelper,
            onBack = { currentScreen = Screen.ModeSelection }
        )
    }
}

enum class Screen {
    ModeSelection,
    BluetoothConnection,
    MasterScreen,
    SlaveScreen
}

enum class Mode {
    Master,
    Slave
}
