package com.hermanowicz.wirelesswhisper.data.model

data class Device(
    val macAddress: String = "",
    val name: String = "",
    val connected: Boolean = false,
    val encryptionKey: String = ""
)
