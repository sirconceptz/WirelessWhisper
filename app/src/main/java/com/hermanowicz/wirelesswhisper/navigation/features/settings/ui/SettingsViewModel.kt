package com.hermanowicz.wirelesswhisper.navigation.features.settings.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermanowicz.wirelesswhisper.bluetooth.BluetoothAction
import com.hermanowicz.wirelesswhisper.bluetooth.MyBluetoothService
import com.hermanowicz.wirelesswhisper.data.model.AppSettings
import com.hermanowicz.wirelesswhisper.domain.ObserveAppSettingsUseCase
import com.hermanowicz.wirelesswhisper.domain.UpdateAppSettingsUseCase
import com.hermanowicz.wirelesswhisper.navigation.features.settings.state.AppSettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val observeAppSettingsUseCase: ObserveAppSettingsUseCase,
    private val updateAppSettingsUseCase: UpdateAppSettingsUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _state = MutableStateFlow(AppSettingsState())
    val state: StateFlow<AppSettingsState> = _state.asStateFlow()

    init {
        observeAppSettings()
    }

    fun observeAppSettings() {
        updateAppSettingsUseCase()
        viewModelScope.launch {
            observeAppSettingsUseCase().collect { appSettings ->
                if(!state.value.bluetoothEnabled && appSettings.bluetoothEnabled)
                    startBTService()
                if(state.value.bluetoothEnabled && !appSettings.bluetoothEnabled)
                    stopBTService()
                updateAppSettings(appSettings)
            }
        }
    }

    private fun stopBTService() {
        BluetoothAction.goToAction(context, MyBluetoothService.ACTION_STOP)
    }

    private fun startBTService() {
        BluetoothAction.goToAction(context, MyBluetoothService.ACTION_START)
    }

    private fun updateAppSettings(appSettings: AppSettings) {
        _state.update {
            it.copy(
                bluetoothEnabled = appSettings.bluetoothEnabled,
                notificationsEnabled = appSettings.notificationsEnabled
            )
        }
    }

    fun openDeviceSettings(bool: Boolean) {
        _state.update { it.copy(openDeviceSettings = bool) }
    }
}
