package com.hermanowicz.wirelesswhisper.data.mapper

import com.hermanowicz.wirelesswhisper.data.model.Message
import com.hermanowicz.wirelesswhisper.data.model.MessageEntity

fun MessageEntity.toDomainModel() = Message(
    id = id,
    senderAddress = senderAddress,
    receiverAddress = receiverAddress,
    message = message,
    timestamp = timestamp,
    received = received,
    readOut = readOut,
    messageStatus = messageStatus
)

fun Message.toEntityModel() = MessageEntity(
    id = id,
    senderAddress = senderAddress,
    receiverAddress = receiverAddress,
    message = message,
    timestamp = timestamp,
    received = received,
    readOut = readOut,
    messageStatus = messageStatus
)
