package com.example.mobileapp_project.channels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobileapp_project.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChannelsViewModel(
    private val channelDao: ChannelDao,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _channels = MutableStateFlow<List<Channel>>(emptyList())
    val channels: StateFlow<List<Channel>> = _channels

    init {
        loadChannels()
    }

    private fun loadChannels() {
        viewModelScope.launch {
            _channels.value = channelDao.getAllChannels()
        }
    }

    fun addChannel(channel: Channel) {
        viewModelScope.launch {
            channelDao.insert(channel)
            loadChannels()
        }
    }

    fun deleteChannel(channel: Channel) {
        viewModelScope.launch {
            channelDao.delete(channel)
            loadChannels()
        }
    }

    // Handle setup preferences
    fun isSetupCompleted() = preferencesManager.isSetupComplete()

    fun setSetupCompleted(isCompleted: Boolean) = preferencesManager.setSetupComplete(isCompleted)

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
