package com.hermanowicz.wirelesswhisper.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "message"
)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val senderAddress: String = "",
    val receiverAddress: String = "",
    val message: String = "",
    val timestamp: Long = 0L,
    val received: Boolean = false,
    val readOut: Boolean = false,
    val error: Boolean = false
)
