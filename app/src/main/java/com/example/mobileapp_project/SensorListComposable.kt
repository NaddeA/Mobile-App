// SensorListComposable.kt
package com.example.mobileapp_project

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SensorList(sensorList: List<SensorItem>, onItemClick: (SensorItem) -> Unit) {
    LazyColumn {
        items(sensorList) { sensorItem ->
            SensorItemCard(sensorItem, onItemClick)
        }
    }
}

@Composable
fun SensorItemCard(sensorItem: SensorItem, onItemClick: (SensorItem) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(sensorItem) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = sensorItem.title, style = MaterialTheme.typography.titleMedium)
            Text(text = sensorItem.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}



