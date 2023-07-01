package com.hermanowicz.wirelesswhisper.domain

import com.hermanowicz.wirelesswhisper.di.repository.DeviceRepository
import javax.inject.Inject

class UpdateDeviceConnectionStatusUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) : suspend (String, Boolean) -> Unit {
    override suspend fun invoke(macAddress: String, connectionStatus: Boolean) {
        deviceRepository.updateConnectionStatus(macAddress, connectionStatus)
    }
}
