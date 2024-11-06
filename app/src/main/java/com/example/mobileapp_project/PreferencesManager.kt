package com.example.mobileapp_project

import android.content.Context
import android.content.SharedPreferences

// This class manages user preferences for the app, allowing you to store and retrieve
// settings using SharedPreferences. It enables saving and accessing setup state,
// channel count, and specific settings for individual channels.
class PreferencesManager(context: Context) {

    // Name for SharedPreferences file where preferences are saved
    private val SHARED_PREFS_NAME = "Setup_prefs"
    // SharedPreferences instance for reading data
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    // Editor instance for writing data
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    // Save whether the setup has been completed (boolean value)
    fun setSetupComplete(isCompleted: Boolean) {
        editor.putBoolean("isSetupCompleted", isCompleted) // Save the setup completion status
        editor.apply() // Apply the change to make it persistent
    }

    // Retrieve the setup completion status, defaulting to 'false' if not set
    fun isSetupComplete(): Boolean {
        return sharedPreferences.getBoolean("isSetupCompleted", false)
    }

    // Save whether the app is in "master" mode (boolean value)
    fun setMaster(isMaster: Boolean) {
        editor.putBoolean("isMaster", isMaster)
        editor.apply()
    }

    // Retrieve the master mode status, defaulting to 'false' if not set
    fun isMaster(): Boolean {
        return sharedPreferences.getBoolean("isMaster", false)
    }

    // Save the number of channels as an integer
    fun setNumberOfChannels(numberOfChannels: Int) {
        editor.putInt("numberOfChannels", numberOfChannels)
        editor.apply()
    }

    // Retrieve the number of channels, defaulting to 1 if not set
    fun getNumberOfChannels(): Int {
        return sharedPreferences.getInt("numberOfChannels", 1)
    }

    // Save a custom name for a specific channel based on its ID
    fun setChannelName(channelId: Int, name: String) {
        // Use "channel_name_<ID>" format to uniquely identify each channel's name
        editor.putString("channel_name_$channelId", name)
        editor.apply()
    }

    // Retrieve the custom name for a specific channel, defaulting to "Channel <ID>" if not set
    fun getChannelName(channelId: Int): String {
        // Use "channel_name_<ID>" key to fetch each channel's name, with a default fallback
        return sharedPreferences.getString("channel_name_$channelId", "Channel $channelId") ?: "Channel $channelId"
    }

    // Save a trigger level for a specific channel based on its ID
    fun setTriggerLevel(channelId: Int, level: Float) {
        // Use "trigger_level_<ID>" format for each channel's trigger level
        editor.putFloat("trigger_level_$channelId", level)
        editor.apply()
    }

    // Retrieve the trigger level for a specific channel, defaulting to 0.0f if not set
    fun getTriggerLevel(channelId: Int): Float {
        return sharedPreferences.getFloat("trigger_level_$channelId", 0.0f)
    }

    // Save an alarm type (e.g., "Sound", "Vibration", etc.) for a specific channel
    fun setAlarmType(channelId: Int, alarmType: String) {
        // Use "alarm_type_<ID>" format to uniquely store each channel's alarm type
        editor.putString("alarm_type_$channelId", alarmType)
        editor.apply()
    }

    // Retrieve the alarm type for a specific channel, defaulting to "None" if not set
    fun getAlarmType(channelId: Int): String {
        return sharedPreferences.getString("alarm_type_$channelId", "None") ?: "None"
    }

    // Clear all saved preferences, removing all data from SharedPreferences
    fun clearPreferences() {
        editor.clear()
        editor.apply()
    }
}