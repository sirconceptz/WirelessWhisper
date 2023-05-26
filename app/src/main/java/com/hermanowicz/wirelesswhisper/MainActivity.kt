package com.hermanowicz.wirelesswhisper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hermanowicz.wirelesswhisper.navigation.AppNavHost
import com.hermanowicz.wirelesswhisper.ui.theme.WirelessWhisperTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WirelessWhisperTheme {
                AppNavHost()
            }
        }
    }
}
