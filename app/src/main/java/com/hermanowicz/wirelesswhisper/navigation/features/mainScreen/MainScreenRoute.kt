package com.hermanowicz.wirelesswhisper.navigation.features.mainScreen

import androidx.compose.runtime.Composable
import com.hermanowicz.wirelesswhisper.navigation.features.mainScreen.ui.MainScreen

@Composable
fun MainScreenRoute(
    onNavigateToPairedDevices: () -> Unit,
    onNavigateToScanDevices: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    MainScreen(
        onNavigateToPairedDevices = onNavigateToPairedDevices,
        onNavigateToScanDevices = onNavigateToScanDevices,
        onNavigateToSettings = onNavigateToSettings
    )
}
