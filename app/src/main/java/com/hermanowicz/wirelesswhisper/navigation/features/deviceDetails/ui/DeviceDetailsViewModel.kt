package com.hermanowicz.wirelesswhisper.navigation.features.deviceDetails.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.domain.ObserveDeviceForAddressUseCase
import com.hermanowicz.wirelesswhisper.navigation.features.deviceDetails.state.DeviceDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val observeDeviceForAddressUseCase: ObserveDeviceForAddressUseCase
) : ViewModel() {
    val macAddress: String = savedStateHandle["macAddress"] ?: ""

    private val _state: MutableStateFlow<DeviceDetailsUiState> = MutableStateFlow(
        DeviceDetailsUiState()
    )
    val state: StateFlow<DeviceDetailsUiState> = _state

    init {
        viewModelScope.launch {
            observeDeviceForAddressUseCase(macAddress).collect { device ->
                updateDeviceState(device)
            }
        }
    }

    private fun updateDeviceState(device: Device) {
        _state.update { it.copy(device = device) }
    }

    fun onGoToPermissionSettings(bool: Boolean) {
        _state.update {
            it.copy(goToPermissionSettings = bool)
        }
    }

    fun showDialogPermissionsConnectDevice(bool: Boolean) {
        _state.update {
            it.copy(showDialogPermissionsConnectDevice = bool)
        }
    }

    fun showDialogPermissionsDisconnectDevice(bool: Boolean) {
        _state.update {
            it.copy(showDialogPermissionsDisconnectDevice = bool)
        }
    }
}
