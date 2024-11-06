package com.example.mobileapp_project.Bluetooth.di

import android.content.Context
import com.example.mobileapp_project.Bluetooth.data.chat.AndroidBluetoothController
import com.example.mobileapp_project.Bluetooth.domain.chat.BluetoothController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Dagger module for providing BluetoothController dependency
@Module
@InstallIn(SingletonComponent::class) // Install the module in SingletonComponent, meaning it's available throughout the app
object AppModule {

    // Provides a single instance of BluetoothController for the entire application
    @Provides
    @Singleton
    fun provideBluetoothController(@ApplicationContext context: Context): BluetoothController {
        return AndroidBluetoothController(context) // Returns the implementation of BluetoothController
    }
}
