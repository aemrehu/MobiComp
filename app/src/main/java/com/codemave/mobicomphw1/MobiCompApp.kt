package com.codemave.mobicomphw1

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.codemave.mobicomphw1.ui.home.HomeScreen
import com.codemave.mobicomphw1.ui.home.notifications.AddNotification
import com.codemave.mobicomphw1.ui.home.profile.ProfileScreen
import com.codemave.mobicomphw1.ui.login.LoginScreen

@Composable
fun MobiCompApp(
    appState: MobiCompAppState = rememberMobiCompAppState(),
    context: Context
) {
    NavHost(
        navController = appState.navController,
        startDestination = "login"
    ) {
        composable(route = "login") {
            LoginScreen(navController = appState.navController, context = context)
        }
        composable(route = "home") {
            HomeScreen(navController = appState.navController)
        }
        composable(route = "profile") {
            ProfileScreen(navController = appState.navController)
        }
        composable(route = "add") {
            AddNotification(navController = appState.navController)
        }
    }
}