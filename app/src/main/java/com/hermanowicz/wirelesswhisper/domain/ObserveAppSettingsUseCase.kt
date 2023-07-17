package com.hermanowicz.wirelesswhisper.domain

import com.hermanowicz.wirelesswhisper.data.model.AppSettings
import com.hermanowicz.wirelesswhisper.di.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAppSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : () -> Flow<AppSettings> {
    override fun invoke(): Flow<AppSettings> {
        return settingsRepository.appSettings
    }
}