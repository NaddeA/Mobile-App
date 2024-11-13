package com.example.mobileapp_project.ui.theme.UI

// Composable class

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BluetoothSettingsScreen(
    onEnableBluetooth: () -> Unit,
    onMasterMode: () -> Unit,
    onSlaveMode: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        BluetoothOptionCard(
            text = "Enable Bluetooth",  // Replace stringResource with a direct string
            onClick = onEnableBluetooth
        )
        Spacer(modifier = Modifier.height(16.dp))
        BluetoothOptionCard(
            text = "Activate Master Mode",  // Replace stringResource with a direct string
            onClick = onMasterMode
        )
        Spacer(modifier = Modifier.height(16.dp))
        BluetoothOptionCard(
            text = "Activate Slave Mode",  // Replace stringResource with a direct string
            onClick = onSlaveMode
        )
    }
}

@Composable
fun BluetoothOptionCard(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BluetoothEnableButton(
    onBluetoothEnable: () -> Unit,
    isBluetoothEnabled: Boolean
) {
    val context = LocalContext.current as? Activity
    Button(
        onClick = {
                onBluetoothEnable()
        },
        enabled = !isBluetoothEnabled,
        modifier = Modifier.fillMaxWidth()
    ) {
        androidx.compose.material.Text("Enable Bluetooth")
    }
}
