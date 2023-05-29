package com.hermanowicz.wirelesswhisper.navigation.features.pairedDevices

import androidx.compose.runtime.Composable
import com.hermanowicz.wirelesswhisper.navigation.features.pairedDevices.ui.PairedDevicesScreen

@Composable
fun PairedDevicesRoute(
    onClickPairedDevice: (String) -> Unit
) {
    PairedDevicesScreen(
        onClickPairedDevice = onClickPairedDevice
    )
}