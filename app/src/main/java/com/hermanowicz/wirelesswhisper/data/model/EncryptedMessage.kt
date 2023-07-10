package com.hermanowicz.wirelesswhisper.data.model

data class EncryptedMessage(
    val message: String,
    val timestamp: Long,
    val iv: ByteArray,
    val senderAddress: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EncryptedMessage

        if (!message.contentEquals(other.message)) return false
        if (timestamp != other.timestamp) return false
        if (!iv.contentEquals(other.iv)) return false
        if (senderAddress != other.senderAddress) return false

        return true
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + iv.contentHashCode()
        result = 31 * result + senderAddress.hashCode()
        return result
    }
}
