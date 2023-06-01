package com.hermanowicz.wirelesswhisper.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "device"
)
data class DeviceEntity(
    @PrimaryKey
    val macAddress: String = "",
    val name: String = ""
)
