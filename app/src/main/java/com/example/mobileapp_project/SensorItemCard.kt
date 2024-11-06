package com.example.mobileapp_project

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SensorItemCard(name: String, type: String) {
    Card(
        modifier = Modifier.padding(8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = name, style = androidx.compose.material.MaterialTheme.typography.h6)
            Text(text = type, style = androidx.compose.material.MaterialTheme.typography.body2)
        }
    }
}

@Preview
@Composable
fun PreviewSensorItemCard() {
    SensorItemCard(name = "Accelerometer", type = "Type: 1")
}
