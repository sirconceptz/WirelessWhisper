package com.hermanowicz.wirelesswhisper.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.SnackbarDefaults
import androidx.compose.material.contentColorFor
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hermanowicz.wirelesswhisper.navigation.features.allChats.AllChatsRoute
import com.hermanowicz.wirelesswhisper.navigation.features.deviceDetails.DeviceDetailsRoute
import com.hermanowicz.wirelesswhisper.navigation.features.mainScreen.MainScreenRoute
import com.hermanowicz.wirelesswhisper.navigation.features.pairedDevices.PairedDevicesRoute
import com.hermanowicz.wirelesswhisper.navigation.features.scanDevices.ScanDevicesRoute
import com.hermanowicz.wirelesswhisper.navigation.features.settings.SettingsRoute

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
                    onNavigateToSettings = { navController.navigate(AppScreens.Settings.route) },
                    bottomBar = { BottomNav(navController = navController) }
                )
            }
            composable(route = AppScreens.PairedDevices.route) {
                PairedDevicesRoute(
                    onClickPairedDevice = { navController.navigate("${AppScreens.DeviceDetails.route}/$it") },
                    bottomBar = { BottomNav(navController = navController) }
                )
            }
            composable(route = "${AppScreens.DeviceDetails.route}/{macAddress}") {
                DeviceDetailsRoute(
                    bottomBar = { BottomNav(navController = navController) }
                )
            }
            composable(route = AppScreens.ScanDevices.route) {
                ScanDevicesRoute(
                    bottomBar = { BottomNav(navController = navController) }
                )
            }
            composable(route = AppScreens.AllChats.route) {
                AllChatsRoute(
                    bottomBar = { BottomNav(navController = navController) }
                )
            }
            composable(route = AppScreens.Settings.route) {
                SettingsRoute(
                    bottomBar = { BottomNav(navController = navController) }
                )
            }
        }
    }
}

@Composable
fun BottomNav(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val items = listOf(
        AppScreens.MainScreen,
        AppScreens.AllChats,
        AppScreens.Settings
    )
    NavigationBar(
        contentColor = contentColorFor(SnackbarDefaults.backgroundColor),
        containerColor = Color.White
    ) {
        items.forEach { item ->
            val title = stringResource(item.titleResId)
            NavigationBarItem(
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = title
                    )
                },
                label = {
                    Text(
                        text = title,
                        fontSize = 9.sp
                    )
                },
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route)
                }
            )
        }
    }
}
