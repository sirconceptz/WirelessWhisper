package com.hermanowicz.wirelesswhisper.domain

import com.hermanowicz.wirelesswhisper.di.repository.DeviceRepository
import javax.inject.Inject

class UpdateDeviceNameUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) : suspend (String, String) -> Unit {
    override suspend fun invoke(macAddress: String, newName: String) {
        deviceRepository.updateName(macAddress, newName)
    }
}
