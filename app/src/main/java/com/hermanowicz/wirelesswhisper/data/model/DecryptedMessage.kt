package com.hermanowicz.wirelesswhisper.data.model

data class DecryptedMessage(
    val message: String,
    val timestamp: Long,
    val senderMacAddress: String
)
