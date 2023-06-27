package com.hermanowicz.wirelesswhisper.data.mapper

import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.data.model.DeviceEntity

fun DeviceEntity.toDomainModel() = Device(
    name = name,
    macAddress = macAddress,
    connected = connected
)

fun Device.toEntityModel() = DeviceEntity(
    name = name,
    macAddress = macAddress,
    connected = connected
)
