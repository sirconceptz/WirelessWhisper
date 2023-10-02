package com.hermanowicz.wirelesswhisper.di.repository

import com.hermanowicz.wirelesswhisper.data.model.AppSettings
import com.hermanowicz.wirelesswhisper.data.repository.SettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val isPushNotificationsEnabled: StateFlow<Boolean>
    val appSettings: Flow<AppSettings>
    fun updateAppSettings()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsRepositoryModule {

    @Binds
    abstract fun bindSettingsRepositorySource(
        settingsRepositorySourceImpl: SettingsRepositoryImpl
    ): SettingsRepository
}
