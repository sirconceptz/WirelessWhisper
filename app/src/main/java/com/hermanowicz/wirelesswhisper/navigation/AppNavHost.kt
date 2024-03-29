package com.hermanowicz.wirelesswhisper.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
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
import com.hermanowicz.wirelesswhisper.navigation.features.pairedDevices.PairedDevicesRoute
import com.hermanowicz.wirelesswhisper.navigation.features.scanDevices.ScanDevicesRoute
import com.hermanowicz.wirelesswhisper.navigation.features.settings.SettingsRoute
import com.hermanowicz.wirelesswhisper.navigation.features.singleChat.SingleChatRoute

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = AppScreens.AllChats.route
        ) {
            composable(route = AppScreens.PairedDevices.route) {
                PairedDevicesRoute(
                    onClickPairedDevice = { navController.navigate("${AppScreens.DeviceDetails.route}/$it") },
                    navigateToScanDevices = { navController.navigate(AppScreens.ScanDevices.route) },
                    bottomBar = { BottomNav(navController = navController) }
                )
            }
            composable(route = "${AppScreens.DeviceDetails.route}/{macAddress}") {
                DeviceDetailsRoute(
                    bottomBar = { BottomNav(navController = navController) },
                    navBack = { navController.popBackStack() }
                )
            }
            composable(route = AppScreens.ScanDevices.route) {
                ScanDevicesRoute(
                    bottomBar = { BottomNav(navController = navController) }
                )
            }
            composable(route = AppScreens.AllChats.route) {
                AllChatsRoute(
                    onClickSingleChat = { device ->
                        if (device != null) {
                            navController.navigate("${AppScreens.SingleChat.route}/${device.macAddress}")
                        }
                    },
                    bottomBar = { BottomNav(navController = navController) }
                )
            }
            composable(route = "${AppScreens.SingleChat.route}/{macAddress}") {
                SingleChatRoute(
                    navigationIconClick = { navController.popBackStack() }
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
        AppScreens.AllChats,
        AppScreens.PairedDevices,
        AppScreens.Settings
    )
    NavigationBar(
        contentColor = contentColorFor(SnackbarDefaults.contentColor),
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
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route)
                }
            )
        }
    }
}
