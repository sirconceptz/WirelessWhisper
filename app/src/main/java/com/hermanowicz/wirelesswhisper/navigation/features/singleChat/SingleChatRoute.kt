package com.hermanowicz.wirelesswhisper.navigation.features.singleChat

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.hermanowicz.wirelesswhisper.bluetooth.MyBluetoothService
import com.hermanowicz.wirelesswhisper.navigation.features.singleChat.ui.SingleChatScreen

@Composable
fun SingleChatRoute(
    bottomBar: @Composable () -> Unit
) {
    val bluetoothServiceIntent = Intent(LocalContext.current, MyBluetoothService::class.java)

    SingleChatScreen(
        bottomBar = bottomBar,
        bluetoothService = bluetoothServiceIntent
    )
}
