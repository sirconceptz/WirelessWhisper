package com.hermanowicz.wirelesswhisper.data.model

data class Message (
    val id: Int?,
    val senderAddress: String = "",
    val receiverAddress: String = "",
    val message: String = "",
    val timestamp: String = ""
)