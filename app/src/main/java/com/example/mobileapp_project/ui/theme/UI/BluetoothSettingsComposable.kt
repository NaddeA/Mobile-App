package com.example.mobileapp_project.ui.theme.UI

//Composable file made just for one function


import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BluetoothSettingsComposable(
    isBluetoothEnabled: Boolean,
    onEnableBluetoothClick: () -> Unit,
    onActivateMasterModeClick: () -> Unit,
    onActivateSlaveModeClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        BluetoothEnableButton({onEnableBluetoothClick()},isBluetoothEnabled)
        Spacer(modifier = Modifier.height(16.dp))
        BluetoothOptionCard(
            text = "Activate Master Mode",
            onClick = { onActivateMasterModeClick() }
        )
        Spacer(modifier = Modifier.height(16.dp))
        BluetoothOptionCard(
            text = "Activate Slave Mode",
            onClick = { onActivateSlaveModeClick() }
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { onBackClick() }) {
            Text("Back")
        }
    }
}
