package com.hermanowicz.wirelesswhisper.data.model

data class Message(
    val id: Int?,
    val senderAddress: String = "",
    val receiverAddress: String = "",
    val message: String = "",
    val timestamp: Long = 0L,
    val received: Boolean = false,
    val readOut: Boolean = false,
    val error: Boolean = false
)
