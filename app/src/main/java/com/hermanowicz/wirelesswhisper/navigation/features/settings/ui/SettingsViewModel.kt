package com.hermanowicz.wirelesswhisper.navigation.features.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermanowicz.wirelesswhisper.data.model.AppSettings
import com.hermanowicz.wirelesswhisper.domain.ObserveAppSettingsUseCase
import com.hermanowicz.wirelesswhisper.domain.UpdateAppSettingsUseCase
import com.hermanowicz.wirelesswhisper.navigation.features.settings.state.AppSettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val observeAppSettingsUseCase: ObserveAppSettingsUseCase,
    private val updateAppSettingsUseCase: UpdateAppSettingsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppSettingsState())
    val uiState: StateFlow<AppSettingsState> = _uiState.asStateFlow()

    init {
        observeAppSettings()
    }

    fun observeAppSettings() {
        updateAppSettingsUseCase()
        viewModelScope.launch {
            observeAppSettingsUseCase().collect { appSettings ->
                updateAppSettings(appSettings)
            }
        }
    }

    private fun updateAppSettings(appSettings: AppSettings) {
        _uiState.update {
            it.copy(
                bluetoothEnabled = appSettings.bluetoothEnabled,
                notificationsEnabled = appSettings.notificationsEnabled
            )
        }
    }

    fun openDeviceSettings(bool: Boolean) {
        _uiState.update { it.copy(openDeviceSettings = bool) }
    }
}
