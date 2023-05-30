package com.hermanowicz.wirelesswhisper.data.model

data class EncryptedMessage(
    val message: ByteArray,
    val iv: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EncryptedMessage

        if (!message.contentEquals(other.message)) return false
        if (!iv.contentEquals(other.iv)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + iv.contentHashCode()
        return result
    }
}