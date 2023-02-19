package com.codemave.mobicomphw1

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class MobiCompAppState(
    val navController: NavHostController
) {
    fun navigateBack() {
        navController.popBackStack()
    }
}

@Composable
fun rememberMobiCompAppState(
    navController: NavHostController = rememberNavController()
) = remember(navController) {
    MobiCompAppState(navController)
}