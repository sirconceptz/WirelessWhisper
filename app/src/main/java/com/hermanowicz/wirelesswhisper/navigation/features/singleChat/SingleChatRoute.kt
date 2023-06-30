package com.hermanowicz.wirelesswhisper.navigation.features.singleChat

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.hermanowicz.wirelesswhisper.bluetooth.MyBluetoothService
import com.hermanowicz.wirelesswhisper.navigation.features.singleChat.ui.SingleChatScreen

@Composable
fun SingleChatRoute(
    navigationIconClick: () -> Unit
) {
    val bluetoothServiceIntent = Intent(LocalContext.current, MyBluetoothService::class.java)

    SingleChatScreen(
        navigationIconClick = navigationIconClick,
        bluetoothService = bluetoothServiceIntent
    )
}
