package com.hermanowicz.wirelesswhisper.navigation.features.deviceDetails.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.domain.DeletePairedDeviceUseCase
import com.hermanowicz.wirelesswhisper.domain.ObserveDeviceForAddressUseCase
import com.hermanowicz.wirelesswhisper.navigation.features.deviceDetails.state.DeviceDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val observeDeviceForAddressUseCase: ObserveDeviceForAddressUseCase,
    private val deletePairedDeviceUseCase: DeletePairedDeviceUseCase
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

    private fun updateDeviceState(device: Device?) {
        _state.update { it.copy(device = device) }
    }

    fun onGoToPermissionSettings(bool: Boolean) {
        _state.update {
            it.copy(goToPermissionSettings = bool)
        }
    }

    fun showDialogPermissionsConnectDevice(show: Boolean) {
        _state.update {
            it.copy(showDialogPermissionsConnectDevice = show)
        }
    }

    fun showDialogPermissionsDisconnectDevice(show: Boolean) {
        _state.update {
            it.copy(showDialogPermissionsDisconnectDevice = show)
        }
    }

    fun showDeleteDeviceDialog(show: Boolean) {
        _state.update {
            it.copy(showDeleteDeviceDialog = show)
        }
    }

    private fun goToNavBack() {
        _state.update {
            it.copy(navBack = true)
        }
    }

    fun deleteDeviceConfirmed() {
        viewModelScope.launch(Dispatchers.IO) {
            if (state.value.device != null) {
                deletePairedDeviceUseCase(state.value.device!!.macAddress)
                goToNavBack()
            }
        }
    }
}
