package com.hermanowicz.wirelesswhisper.navigation.features.scanDevices

import androidx.compose.runtime.Composable
import com.hermanowicz.wirelesswhisper.navigation.features.scanDevices.ui.ScanDevicesScreen

@Composable
fun ScanDevicesRoute(
    bottomBar: @Composable () -> Unit
) {
    ScanDevicesScreen(bottomBar = bottomBar)
}
