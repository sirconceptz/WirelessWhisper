package com.hermanowicz.wirelesswhisper.data.model

data class Chat(
    val device: Device,
    val unreadMessages: Int,
    val deviceConnectionStatus: Boolean
)
