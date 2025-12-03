package com.example.android_assignment.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.android_assignment.screens.ChatScreen
import com.example.android_assignment.screens.ExportStateScreen
import com.example.android_assignment.screens.NotificationsScreen

sealed class Screen(val route: String) {
    object Chat : Screen("chat")
    object Notifications : Screen("notifications")
    object ExportState : Screen("export_state")
}

@Composable
fun SoundverseNavigation(
    navController: NavHostController = rememberNavController(),
    initialRoute: String? = null
) {
    val startDestination = initialRoute ?: Screen.Chat.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Chat.route) {
            ChatScreen(navController = navController)
        }
        composable(Screen.Notifications.route) {
            NotificationsScreen(navController = navController)
        }
        composable(Screen.ExportState.route) {
            ExportStateScreen(navController = navController)
        }
    }
}

