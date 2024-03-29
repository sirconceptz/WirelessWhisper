package com.hermanowicz.wirelesswhisper.navigation.features.settings.state

data class AppSettingsState(
    val bluetoothEnabled: Boolean = false,
    val notificationsEnabled: Boolean = false,
    val openDeviceSettings: Boolean = false
)
