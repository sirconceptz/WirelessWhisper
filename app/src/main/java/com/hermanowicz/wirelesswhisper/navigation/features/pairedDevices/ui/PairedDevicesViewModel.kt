package com.hermanowicz.wirelesswhisper.navigation.features.pairedDevices.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.domain.ObserveAllPairedDevicesUseCase
import com.hermanowicz.wirelesswhisper.navigation.features.pairedDevices.state.PairedDevicesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PairedDevicesViewModel @Inject constructor(
    private val observeAllPairedDevicesUseCase: ObserveAllPairedDevicesUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<PairedDevicesUiState> = MutableStateFlow(PairedDevicesUiState())
    val uiState: StateFlow<PairedDevicesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeAllPairedDevicesUseCase().collect { deviceList ->
                updateDeviceList(deviceList)
            }
        }
    }

    private fun updateDeviceList(deviceList: List<Device>) {
        _uiState.update { it.copy(deviceList = deviceList) }
    }
}
