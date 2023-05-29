package com.hermanowicz.wirelesswhisper.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hermanowicz.wirelesswhisper.navigation.features.deviceDetails.DeviceDetailsRoute
import com.hermanowicz.wirelesswhisper.navigation.features.mainScreen.MainScreenRoute
import com.hermanowicz.wirelesswhisper.navigation.features.pairedDevices.PairedDevicesRoute
import com.hermanowicz.wirelesswhisper.navigation.features.scanDevices.ScanDevicesRoute
import com.hermanowicz.wirelesswhisper.navigation.features.settings.SettingsRoute

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
                MainScreenRoute(
                    onNavigateToPairedDevices = { navController.navigate(AppScreens.PairedDevices.route) },
                    onNavigateToScanDevices = { navController.navigate(AppScreens.ScanDevices.route) },
                    onNavigateToSettings = { navController.navigate(AppScreens.Settings.route) }
                )
            }
            composable(route = AppScreens.PairedDevices.route) {
                PairedDevicesRoute(
                    onClickPairedDevice = { navController.navigate("${AppScreens.DeviceDetails.route}/$it") }
                )
            }
            composable(route = "${AppScreens.DeviceDetails.route}/{macAddress}") {
                DeviceDetailsRoute()
            }
            composable(route = AppScreens.ScanDevices.route) {
                ScanDevicesRoute()
            }
            composable(route = AppScreens.Settings.route) {
                SettingsRoute()
            }
        }
    }
}
