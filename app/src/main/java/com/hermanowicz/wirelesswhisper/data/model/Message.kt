package com.hermanowicz.wirelesswhisper.data.model

import com.hermanowicz.wirelesswhisper.utils.enums.MessageStatus

data class Message(
    val id: Int?,
    val senderAddress: String = "",
    val receiverAddress: String = "",
    val message: String = "",
    val timestamp: Long = 0L,
    val received: Boolean = false,
    val readOut: Boolean = false,
    val messageStatus: MessageStatus = MessageStatus.SEND
)
