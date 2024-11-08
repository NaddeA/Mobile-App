package com.example.mobileapp_project.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define colors based on colors.xml values
val PurplePrimary = Color(0xFF6200EE)  // colorPrimary in colors.xml
val PurpleVariant = Color(0xFF3700B3)  // colorPrimaryVariant in colors.xml
val TealSecondary = Color(0xFF03DAC5)  // colorSecondary in colors.xml

private val LightColorScheme = lightColorScheme(
    primary = PurplePrimary,
    primaryContainer = PurpleVariant,
    secondary = TealSecondary,
)

private val DarkColorScheme = darkColorScheme(
    primary = PurplePrimary,
    primaryContainer = PurpleVariant,
    secondary = TealSecondary,
)

@Composable
fun MobileAppProjectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
