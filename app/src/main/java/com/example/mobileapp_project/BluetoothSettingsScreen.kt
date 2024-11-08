package com.example.mobileapp_project

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* // För layoutkomponenter
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobileapp_project.R


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
            text = stringResource(id = R.string.enable_bluetooth_text),
            onClick = onEnableBluetooth
        )
        Spacer(modifier = Modifier.height(16.dp))
        BluetoothOptionCard(
            text = stringResource(id = R.string.master_mode_text),
            onClick = onMasterMode
        )
        Spacer(modifier = Modifier.height(16.dp))
        BluetoothOptionCard(
            text = stringResource(id = R.string.slave_mode_text),
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
            .clickable { onClick() }, // Lägg till klickfunktion här istället för onClick direkt på Card
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

