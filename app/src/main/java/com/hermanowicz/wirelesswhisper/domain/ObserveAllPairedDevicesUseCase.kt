package com.hermanowicz.wirelesswhisper.domain

import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.di.repository.DeviceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class ObserveAllPairedDevicesUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) : () -> Flow<List<Device>> {
    override fun invoke(): Flow<List<Device>> {
        return deviceRepository.observeAll().distinctUntilChanged()
    }
}
