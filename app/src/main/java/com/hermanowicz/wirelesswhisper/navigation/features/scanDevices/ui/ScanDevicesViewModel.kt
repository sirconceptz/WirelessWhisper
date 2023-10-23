package com.hermanowicz.wirelesswhisper.navigation.features.scanDevices.ui

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.domain.CheckIsBluetoothPermissionGrantedUseCase
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
    private val getDeviceListFromHashMapUseCase: GetDeviceListFromHashMapUseCase,
    private val checkIsBluetoothPermissionGrantedUseCase: CheckIsBluetoothPermissionGrantedUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ScanDevicesState())
    val state: StateFlow<ScanDevicesState> = _state.asStateFlow()

    fun scanDevices() {
        startScanningDevicesUseCase()
    }

    fun showDialogOnPairDeviceConfirmation(show: Boolean, device: Device? = null) {
        _state.update {
            if (device != null) {
                it.copy(showDialogOnPairDeviceConfirmation = show, deviceDuringPairing = device)
            } else {
                it.copy(showDialogOnPairDeviceConfirmation = show)
            }
        }
    }

    fun onClickFoundDevice(device: Device) {
        showDialogOnPairDeviceConfirmation(show = true, device = device)
    }

    fun onStartScanning() {
        getDeviceListFromHashMapUseCase.clearFoundDevices()
    }

    fun setFoundDevice(foundDevice: BluetoothDevice) {
        getDeviceListFromHashMapUseCase.addDevice(foundDevice)
        val devices = getDeviceListFromHashMapUseCase()
        if (devices != state.value.foundDevices) {
            updateDevicesState(devices)
        }
    }

    private fun updateDevicesState(devices: List<Device>) {
        _state.update {
            it.copy(foundDevices = devices)
        }
    }

    fun turnOnDiscoverable(bool: Boolean) {
        _state.update {
            it.copy(turnOnDiscoverableMode = bool)
        }
    }

    fun onGoToPermissionSettings(bool: Boolean) {
        _state.update {
            it.copy(goToPermissionSettings = bool)
        }
    }

    fun showDialogPermissionsScanDevices(bool: Boolean) {
        _state.update {
            it.copy(showDialogPermissionsScanDevices = bool)
        }
    }

    fun showDialogPermissionsOnDiscoverable(bool: Boolean) {
        _state.update {
            it.copy(showDialogPermissionsOnDiscoverable = bool)
        }
    }

    fun showDialogPermissionsOnPair(bool: Boolean) {
        _state.update {
            it.copy(showDialogPermissionsOnPair = bool)
        }
    }

    fun isNeededPermissionsGranted() : Boolean {
        return checkIsBluetoothPermissionGrantedUseCase()
    }
}
