package com.example.mobileapp_project.ui.theme.UI

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Master : Screen("master")
    object Slave : Screen("slave")
    object Logs : Screen("logs")
}
