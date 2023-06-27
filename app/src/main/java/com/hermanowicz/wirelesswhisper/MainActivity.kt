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
        bluetoothServiceIntent = Intent(this, MyBluetoothService::class.java)
        bluetoothServiceIntent.action = MyBluetoothService.ACTION_START
        startService(bluetoothServiceIntent)
        setContent {
            WirelessWhisperTheme {
                AppNavHost()
            }
        }
    }
}
