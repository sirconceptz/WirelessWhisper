package com.hermanowicz.wirelesswhisper.domain

import com.hermanowicz.wirelesswhisper.di.repository.DeviceRepository
import javax.inject.Inject

class UpdateEncryptionKeyInDeviceUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) : suspend (String, ByteArray) -> Unit {
    override suspend fun invoke(address: String, key: ByteArray) {
        deviceRepository.updateEncryptionKey(address, key)
    }
}
