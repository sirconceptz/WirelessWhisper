package com.hermanowicz.wirelesswhisper.data.model

data class Chat(
    val deviceName: String,
    val macAddress: String,
    val unreadMessages: Int
)
