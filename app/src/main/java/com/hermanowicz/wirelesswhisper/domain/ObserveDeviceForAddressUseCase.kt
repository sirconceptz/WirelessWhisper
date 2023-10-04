package com.hermanowicz.wirelesswhisper.domain

import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.di.repository.DeviceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class ObserveDeviceForAddressUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) : (String) -> Flow<Device?> {
    override fun invoke(macAddress: String): Flow<Device?> {
        return deviceRepository.observeByMacAddress(macAddress).distinctUntilChanged()
    }
}
