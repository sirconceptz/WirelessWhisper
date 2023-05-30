package com.hermanowicz.wirelesswhisper.navigation.features.allChats

import androidx.compose.runtime.Composable
import com.hermanowicz.wirelesswhisper.navigation.features.allChats.ui.AllChatsScreen

@Composable
fun AllChatsRoute(
    bottomBar: @Composable () -> Unit
) {
    AllChatsScreen(bottomBar = bottomBar)
}
