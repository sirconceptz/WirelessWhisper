package com.hermanowicz.wirelesswhisper.navigation.features.scanDevices.ui

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.domain.GetDeviceListFromHashMapUseCase
import com.hermanowicz.wirelesswhisper.domain.StartScanningDevicesUseCase
import com.hermanowicz.wirelesswhisper.navigation.features.scanDevices.state.ScanDevicesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ScanDevicesViewModel @Inject constructor(
    private val startScanningDevicesUseCase: StartScanningDevicesUseCase,
    private val getDeviceListFromHashMapUseCase: GetDeviceListFromHashMapUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ScanDevicesState())
    val state: StateFlow<ScanDevicesState> = _state.asStateFlow()

    fun scanDevices() {
        startScanningDevicesUseCase()
    }

    fun showDialogOnPairDevice(show: Boolean, device: Device? = null) {
        _state.update {
            it.copy(showDialogOnPairDevice = show, deviceDuringPairing = device)
        }
    }

    fun onClickFoundDevice(device: Device) {
        showDialogOnPairDevice(show = true, device = device)
    }

    fun onPairDevice() {
        showDialogOnPairDevice(false)
    }

    fun setFoundDevice(foundDevice: BluetoothDevice) {
        getDeviceListFromHashMapUseCase.addDevice(foundDevice)
        val devices = getDeviceListFromHashMapUseCase()
        if (devices != state.value.foundDevices) {
            _state.update {
                it.copy(foundDevices = devices)
            }
        }
    }

    fun onStartScanning() {
        getDeviceListFromHashMapUseCase.clearFoundDevices()
    }

    fun turnOnDiscoverable(bool: Boolean) {
        _state.update {
            it.copy(turnOnDiscoverableMode = bool)
        }
    }
}
