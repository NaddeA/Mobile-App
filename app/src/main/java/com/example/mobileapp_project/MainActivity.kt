package com.example.mobileapp_project

import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SensorApp()
        }
    }

    // get the list of sensorss
    private fun getSensorInfo(): List<String> {
        return try {
            // Init the SensorManager
            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

            // Get the list of all available sensors, can be adjusted to few sensors
            val deviceSensors = sensorManager.getSensorList(android.hardware.Sensor.TYPE_ALL)

            if (deviceSensors.isNotEmpty()) {
                deviceSensors.map { sensor -> "Sensor: ${sensor.name}" }
            } else {
                listOf("No sensors available on this device")
            }
        } catch (e: Exception) {
            listOf("Error fetching sensors: ${e.message}")
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SensorApp() {
        var sensorData by remember { mutableStateOf(emptyList<String>()) }
        var isSensorVisible by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Sensor Info") })
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Button(onClick = { // Fetch and update sensor data when button is clicked
                        isSensorVisible = !isSensorVisible

                    if (isSensorVisible){
                        sensorData = getSensorInfo()
                    }

                }) {
                    //shows or hides sensors
                    Text(text = if (isSensorVisible) "Hide Sensors" else "Show Sensors")
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isSensorVisible) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(sensorData) { sensor ->
                            Text(
                                text = sensor,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewSensorApp() {
        SensorApp()
    }
}
