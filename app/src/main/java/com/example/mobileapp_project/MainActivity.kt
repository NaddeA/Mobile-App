package com.example.mobileapp_project

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobileapp_project.Slave.AppSensorManager

class MainActivity : ComponentActivity() {
    private lateinit var appSensorManager: AppSensorManager
    private var sensorDataList = mutableStateListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request WRITE_EXTERNAL_STORAGE permission if not granted
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

        // Initialize AppSensorManager with data collection callback
        appSensorManager = AppSensorManager(
            context = this,
            onSensorDataCollected = { sensorType, x, y, z ->
                val data = "$sensorType: X=$x, Y=$y, Z=$z"
                sensorDataList.add(0, data)
                if (sensorDataList.size > 100) sensorDataList.removeLast() // Limit displayed items
            }
        )

        // Set content for the UI
        setContent {
            SensorApp()
        }
    }

    override fun onResume() {
        super.onResume()
        appSensorManager.startDataCollection() // Start collecting data when activity resumes
    }

    override fun onPause() {
        super.onPause()
        appSensorManager.stopDataCollection() // Stop collecting data when activity pauses
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SensorApp() {
        Scaffold(
            topBar = { TopAppBar(title = { Text("Sensor Info") }) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Start and Stop buttons for manual control
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(onClick = { appSensorManager.startDataCollection() }) {
                        Text("Start Collection")
                    }
                    Button(onClick = { appSensorManager.stopDataCollection() }) {
                        Text("Stop Collection")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display sensor data
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(sensorDataList) { sensor ->
                        Text(text = sensor, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults) // Call super to resolve the warning

        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, proceed with file operations if needed
        } else {
            // Permission denied, handle accordingly (e.g., show a message)
        }
    }
}
