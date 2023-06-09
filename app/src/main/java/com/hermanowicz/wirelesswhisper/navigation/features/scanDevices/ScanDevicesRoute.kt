package com.hermanowicz.wirelesswhisper.navigation.features.scanDevices

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.hermanowicz.wirelesswhisper.bluetooth.MyBluetoothService
import com.hermanowicz.wirelesswhisper.navigation.features.scanDevices.ui.ScanDevicesScreen

@Composable
fun ScanDevicesRoute(
    bottomBar: @Composable () -> Unit
) {
    val bluetoothServiceIntent = Intent(LocalContext.current, MyBluetoothService::class.java)

    ScanDevicesScreen(
        bottomBar = bottomBar,
        bluetoothService = bluetoothServiceIntent
    )
}
