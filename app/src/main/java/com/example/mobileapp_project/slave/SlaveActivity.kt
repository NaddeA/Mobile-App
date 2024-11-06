package com.example.mobileapp_project.slave


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider

import com.example.mobileapp_project.PreferencesManager
import com.example.mobileapp_project.bluetooth.BluetoothHelper
import com.example.mobileapp_project.channels.ChannelsDatabase
import com.example.mobileapp_project.channels.ChannelsViewModel
import com.example.mobileapp_project.ui.screens.SlaveScreen

class SlaveActivity : ComponentActivity() {

    private lateinit var channelsViewModel: ChannelsViewModel
    private lateinit var bluetoothHelper: BluetoothHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = ChannelsDatabase.getDatabase(applicationContext)
        val preferencesManager = PreferencesManager(applicationContext)

        channelsViewModel = ViewModelProvider(
            this,
            ChannelsViewModel.Factory(database.dao(), preferencesManager)
        )[ChannelsViewModel::class.java]

        bluetoothHelper = BluetoothHelper(this)

        setContent {
            SlaveScreen(viewModel = channelsViewModel, bluetoothHelper = bluetoothHelper)
        }
    }
}
