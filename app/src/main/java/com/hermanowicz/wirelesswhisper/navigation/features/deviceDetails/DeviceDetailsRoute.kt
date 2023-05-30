package com.hermanowicz.wirelesswhisper.navigation.features.deviceDetails

import androidx.compose.runtime.Composable
import com.hermanowicz.wirelesswhisper.navigation.features.deviceDetails.ui.DeviceDetailsScreen

@Composable
fun DeviceDetailsRoute(
    bottomBar: @Composable () -> Unit
) {
    DeviceDetailsScreen(bottomBar = bottomBar)
}
