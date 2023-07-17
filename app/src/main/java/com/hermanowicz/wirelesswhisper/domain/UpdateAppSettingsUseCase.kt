package com.hermanowicz.wirelesswhisper.domain

import com.hermanowicz.wirelesswhisper.di.repository.SettingsRepository
import javax.inject.Inject

class UpdateAppSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) : () -> Unit {
    override fun invoke() {
        settingsRepository.updateAppSettings()
    }
}