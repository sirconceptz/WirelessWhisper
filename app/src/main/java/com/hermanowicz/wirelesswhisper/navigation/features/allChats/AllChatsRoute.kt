package com.hermanowicz.wirelesswhisper.navigation.features.allChats

import androidx.compose.runtime.Composable
import com.hermanowicz.wirelesswhisper.navigation.features.allChats.ui.AllChatsScreen

@Composable
fun AllChatsRoute(
    onClickSingleChat : (String) -> Unit,
    bottomBar: @Composable () -> Unit
) {
    AllChatsScreen(
        onClickSingleChat = onClickSingleChat,
        bottomBar = bottomBar
    )
}
