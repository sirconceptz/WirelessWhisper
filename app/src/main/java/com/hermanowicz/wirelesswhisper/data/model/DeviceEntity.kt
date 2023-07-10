package com.hermanowicz.wirelesswhisper.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "device"
)
data class DeviceEntity(
    @PrimaryKey
    val macAddress: String = "",
    val name: String = "",
    val connected: Boolean = false,
    val encryptionKey: ByteArray = byteArrayOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DeviceEntity

        if (macAddress != other.macAddress) return false
        if (name != other.name) return false
        if (connected != other.connected) return false
        if (!encryptionKey.contentEquals(other.encryptionKey)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = macAddress.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + connected.hashCode()
        result = 31 * result + encryptionKey.contentHashCode()
        return result
    }
}
