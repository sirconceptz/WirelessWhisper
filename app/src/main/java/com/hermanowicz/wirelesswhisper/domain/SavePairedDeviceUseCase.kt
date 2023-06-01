package com.hermanowicz.wirelesswhisper.domain

import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.di.repository.DeviceRepository
import javax.inject.Inject

class SavePairedDeviceUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) : suspend (Device) -> Unit {
    override suspend fun invoke(device: Device) {
        deviceRepository.insert(device)
    }
}
