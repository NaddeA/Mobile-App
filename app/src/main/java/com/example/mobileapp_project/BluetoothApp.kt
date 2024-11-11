package com.example.mobileapp_project
// this is just a class that is in place so that the Hilt injection works

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BluetoothApp: Application()