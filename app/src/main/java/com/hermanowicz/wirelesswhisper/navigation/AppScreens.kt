package com.hermanowicz.wirelesswhisper.navigation

import com.hermanowicz.wirelesswhisper.R

sealed class AppScreens(
    val route: String,
    val titleResId: Int,
    val icon: Int
) {
    object DeviceDetails : AppScreens("DEVICE_DETAILS_SCREEN", R.string.device_details, R.drawable.ic_smartphone)
    object AllChats : AppScreens("ALL_CHATS_SCREEN", R.string.all_chats, R.drawable.ic_chat)
    object PairedDevices : AppScreens("PAIRED_DEVICES_SCREEN", R.string.paired_devices, R.drawable.ic_smartphone)
    object ScanDevices : AppScreens("SCAN_DEVICES_SCREEN", R.string.scan_devices, R.drawable.ic_pair_bt)
    object Settings : AppScreens("SETTINGS_SCREEN", R.string.settings, R.drawable.ic_settings)
}
