package com.hermanowicz.wirelesswhisper.navigation.features.settings

import androidx.compose.runtime.Composable
import com.hermanowicz.wirelesswhisper.navigation.features.settings.ui.SettingsScreen

@Composable
fun SettingsRoute(
    bottomBar: @Composable () -> Unit
) {
    SettingsScreen(
        bottomBar = bottomBar
    )
}
