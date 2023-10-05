package com.hermanowicz.wirelesswhisper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hermanowicz.wirelesswhisper.bluetooth.BluetoothAction
import com.hermanowicz.wirelesswhisper.bluetooth.MyBluetoothService
import com.hermanowicz.wirelesswhisper.navigation.AppNavHost
import com.hermanowicz.wirelesswhisper.ui.theme.WirelessWhisperTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BluetoothAction.goToAction(applicationContext, MyBluetoothService.ACTION_START)
        setContent {
            WirelessWhisperTheme {
                AppNavHost()
            }
        }
    }

    override fun onDestroy() {
        BluetoothAction.goToAction(
            applicationContext,
            MyBluetoothService.ACTION_DISCONNECT_ALL_DEVICES
        )
        BluetoothAction.goToAction(
            applicationContext,
            MyBluetoothService.ACTION_STOP
        )
        super.onDestroy()
    }
}
