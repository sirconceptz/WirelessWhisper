package com.hermanowicz.wirelesswhisper.domain

import com.hermanowicz.wirelesswhisper.di.repository.DeviceRepository
import javax.inject.Inject

class DeletePairedDeviceUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) : suspend (String) -> Unit {
    override suspend fun invoke(macAddress: String) {
        deviceRepository.deleteDevice(macAddress)
    }
}
