package com.example.mobileapp_project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChannelsViewModel(
    private val channelDao: ChannelDao,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    // StateFlow to manage the list of channels for the UI to observe
    private val _channels = MutableStateFlow<List<Channel>>(emptyList())
    val channels: StateFlow<List<Channel>> = _channels

    init {
        loadChannels()
    }

    // Function to load channels from the database
    private fun loadChannels() {
        viewModelScope.launch {
            _channels.value = channelDao.getAllChannels()
        }
    }

    // Preference-based methods

    // Check if setup is completed
    fun isSetupCompleted(): Boolean {
        return preferencesManager.isSetupComplete()
    }

    // Set setup completion status
    fun setSetupCompleted(isCompleted: Boolean) {
        preferencesManager.setSetupComplete(isCompleted)
    }

    // Get the number of channels
    fun getNumberOfChannels(): Int {
        return preferencesManager.getNumberOfChannels()
    }

    // Set the number of channels
    fun setNumberOfChannels(numberOfChannels: Int) {
        preferencesManager.setNumberOfChannels(numberOfChannels)
    }

    // Factory to create an instance of ChannelsViewModel with ChannelDao and PreferencesManager
    class Factory(
        private val channelDao: ChannelDao,
        private val preferencesManager: PreferencesManager
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ChannelsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ChannelsViewModel(channelDao, preferencesManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}