package com.hermanowicz.wirelesswhisper.navigation.features.allChats.state

data class AllChatsUiState(
    val chatList: List<Pair<String, String>> = emptyList()
)
