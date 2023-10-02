package com.hermanowicz.wirelesswhisper.data.repository

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import com.hermanowicz.wirelesswhisper.data.model.AppSettings
import com.hermanowicz.wirelesswhisper.di.repository.SettingsRepository
import com.hermanowicz.wirelesswhisper.domain.CheckIsBluetoothPermissionGrantedUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val checkIsBluetoothPermissionGrantedUseCase: CheckIsBluetoothPermissionGrantedUseCase
) : SettingsRepository {

    private val notificationManagerCompat = NotificationManagerCompat.from(context)

    private val _isPushNotificationsEnabled: MutableStateFlow<Boolean> =
        MutableStateFlow(isNotificationsEnabled())
    override val isPushNotificationsEnabled: StateFlow<Boolean> =
        _isPushNotificationsEnabled.asStateFlow()

    override val appSettings: Flow<AppSettings>
        get() =
            isPushNotificationsEnabled.map { pushNotifications ->
                val bluetoothEnabled = checkIsBluetoothPermissionGrantedUseCase()

                AppSettings(
                    notificationsEnabled = pushNotifications,
                    bluetoothEnabled = bluetoothEnabled
                )
            }.distinctUntilChanged()

    override fun updateAppSettings() {
        _isPushNotificationsEnabled.update {
            isNotificationsEnabled()
        }
    }

    private fun isNotificationsEnabled(): Boolean {
        return notificationManagerCompat.areNotificationsEnabled()
    }
}
