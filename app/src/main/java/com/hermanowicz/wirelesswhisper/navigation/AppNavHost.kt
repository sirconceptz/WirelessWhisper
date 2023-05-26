package com.hermanowicz.wirelesswhisper.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hermanowicz.wirelesswhisper.navigation.features.mainScreen.MainScreenRoute

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = AppScreens.MainScreen.route
        ) {
            composable(route = AppScreens.MainScreen.route) {
                MainScreenRoute()
            }
        }
    }
}
