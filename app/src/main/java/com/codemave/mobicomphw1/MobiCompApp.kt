package com.codemave.mobicomphw1

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.codemave.mobicomphw1.ui.home.HomeScreen
import com.codemave.mobicomphw1.ui.home.notifications.AddNotification
import com.codemave.mobicomphw1.ui.home.notifications.EditNotification
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
            HomeScreen(navController = appState.navController, context)
        }
        composable(route = "profile") {
            ProfileScreen(navController = appState.navController, context)
        }
        composable(route = "add") {
            AddNotification(navController = appState.navController, context)
        }
        composable(route = "edit/{id}") {
            val id = it.arguments?.getString("id")
            id?.let {
                EditNotification(id = id.toLong(), appState.navController, context)
            }
        }
    }
}