package com.hermanowicz.wirelesswhisper

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hermanowicz.wirelesswhisper.bluetooth.MyBluetoothService
import com.hermanowicz.wirelesswhisper.navigation.AppNavHost
import com.hermanowicz.wirelesswhisper.ui.theme.WirelessWhisperTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var bluetoothServiceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setServiceAction(MyBluetoothService.ACTION_START)
        setContent {
            WirelessWhisperTheme {
                AppNavHost()
            }
        }
    }

    private fun setServiceAction(action: String) {
        bluetoothServiceIntent = Intent(applicationContext, MyBluetoothService::class.java)
        bluetoothServiceIntent.action = action
        startForegroundService(bluetoothServiceIntent)
    }

    override fun onDestroy() {
        setServiceAction(MyBluetoothService.ACTION_DISCONNECT_ALL_DEVICES)
        super.onDestroy()
    }
}
