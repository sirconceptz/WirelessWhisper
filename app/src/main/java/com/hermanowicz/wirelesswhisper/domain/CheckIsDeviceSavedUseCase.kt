package com.hermanowicz.wirelesswhisper.domain

import com.hermanowicz.wirelesswhisper.di.repository.DeviceRepository
import javax.inject.Inject

class CheckIsDeviceSavedUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) : (String) -> Boolean {
    override fun invoke(macAddress: String): Boolean {
        return deviceRepository.checkIsExist(macAddress)
    }
}
