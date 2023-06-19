package com.hermanowicz.wirelesswhisper.navigation.features.settings.ui

import androidx.lifecycle.ViewModel
import com.hermanowicz.wirelesswhisper.domain.CheckIsBluetoothPermissionGrantedUseCase
import com.hermanowicz.wirelesswhisper.navigation.features.settings.state.AppSettingsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val checkIsBluetoothPermissionGrantedUseCase: CheckIsBluetoothPermissionGrantedUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppSettingsState())
    val uiState: StateFlow<AppSettingsState> = _uiState.asStateFlow()

    init {
        updateBluetoothStatus()
    }

    private fun updateBluetoothStatus() {
        val bluetoothGranted = checkIsBluetoothPermissionGrantedUseCase()
        _uiState.update { it.copy(bluetoothEnabled = bluetoothGranted) }
    }

    fun updateAppSettings() {
        updateBluetoothStatus()
    }

    fun openDeviceSettings(bool: Boolean) {
        _uiState.update { it.copy(openDeviceSettings = bool) }
    }
}
