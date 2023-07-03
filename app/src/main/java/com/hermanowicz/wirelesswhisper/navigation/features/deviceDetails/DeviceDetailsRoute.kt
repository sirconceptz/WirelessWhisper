package com.hermanowicz.wirelesswhisper.navigation.features.deviceDetails

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.hermanowicz.wirelesswhisper.bluetooth.MyBluetoothService
import com.hermanowicz.wirelesswhisper.navigation.features.deviceDetails.ui.DeviceDetailsScreen

@Composable
fun DeviceDetailsRoute(
    bottomBar: @Composable () -> Unit
) {
    val bluetoothServiceIntent = Intent(LocalContext.current, MyBluetoothService::class.java)

    DeviceDetailsScreen(
        bottomBar = bottomBar,
        bluetoothService = bluetoothServiceIntent
    )
}
