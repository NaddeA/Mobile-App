package com.example.mobileapp_project
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
// import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database and preferences manager
        val database = ChannelsDatabase.getDatabase(applicationContext)
        val preferencesManager = PreferencesManager(applicationContext)

        // Create ViewModel with the custom factory
        val viewModel: ChannelsViewModel = ViewModelProvider(
            this,
            ChannelsViewModel.Factory(database.dao(), preferencesManager)
        )[ChannelsViewModel::class.java]

        // Set the content to Compose UI
        setContent {
            MaterialTheme {
                // Call the main screen content
                MainScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: ChannelsViewModel) {
    // Access preferences and database through the ViewModel
    var setupCompleted by remember { mutableStateOf(viewModel.isSetupCompleted()) }
    var channelCount by remember { mutableStateOf(viewModel.getNumberOfChannels()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Channels Setup", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        // Display setup completed status
        Text("Setup Completed: ${if (setupCompleted) "Yes" else "No"}")

        // Display current number of channels
        Text("Number of Channels: $channelCount")

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            // Toggle setup complete status
            setupCompleted = !setupCompleted
            viewModel.setSetupCompleted(setupCompleted)
        }) {
            Text(if (setupCompleted) "Reset Setup" else "Complete Setup")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            // Increment channel count
            if (channelCount < 32) {
                channelCount++
                viewModel.setNumberOfChannels(channelCount)
            }
        }) {
            Text("Add Channel")
        }
    }
}