// SensorListComposable.kt
package com.example.mobileapp_project.ui.theme.UI


// Composable presenting the sensors as a list
import SensorItemCard
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.example.mobileapp_project.Sensor.SensorItem


@Composable
fun SensorList(sensorList: List<SensorItem>, onItemClick: (SensorItem) -> Unit) {
    var index: Int = 0
    LazyColumn {
        items(sensorList) { sensorItem ->
            index ++
            SensorItemCard(sensorItem,index, onItemClick)

        }
    }
}

//@Composable
//fun SensorItemCard(sensorItem: SensorItem, onItemClick: (SensorItem) -> Unit) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .clickable { onItemClick(sensorItem) },
//        elevation = CardDefaults.cardElevation(4.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(text = sensorItem.title, style = MaterialTheme.typography.titleMedium)
//            Text(text = sensorItem.description, style = MaterialTheme.typography.bodyMedium)
//        }
//    }
//}
