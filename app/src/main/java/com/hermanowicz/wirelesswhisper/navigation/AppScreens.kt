package com.hermanowicz.wirelesswhisper.navigation

sealed class AppScreens(
    val route: String
) {
    object MainScreen : AppScreens("MAIN_SCREEN")
    object DeviceDetails : AppScreens("DEVICE_DETAILS_SCREEN")
    object PairedDevices : AppScreens("PAIRED_DEVICES_SCREEN")
    object ScanDevices : AppScreens("SCAN_DEVICES_SCREEN")
    object Settings : AppScreens("SETTINGS_SCREEN")
}
