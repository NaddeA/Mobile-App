package com.example.mobileapp_project.ui.component

import android.Manifest
import android.os.Build
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun RequestBluetoothPermissions(onPermissionsGranted: @Composable () -> Unit) {
    val context = LocalContext.current
    val permissionsGranted = remember { mutableStateOf(false) }

    // List of permissions based on Android version
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        listOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )
    } else {
        listOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    // Define the permission launcher
    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsResult ->
        // Check if all permissions were granted
        permissionsGranted.value = permissionsResult.values.all { it }
    }

    // Function to check if all permissions are granted
    fun arePermissionsGranted(context: Context): Boolean {
        return permissions.all { perm ->
            ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED
        }
    }

    // Launch permission request if needed
    LaunchedEffect(Unit) {
        if (arePermissionsGranted(context)) {
            permissionsGranted.value = true
        } else {
            permissionsLauncher.launch(permissions.toTypedArray())
        }
    }

    // Display content if permissions are granted
    if (permissionsGranted.value) {
        onPermissionsGranted()
    }
}
