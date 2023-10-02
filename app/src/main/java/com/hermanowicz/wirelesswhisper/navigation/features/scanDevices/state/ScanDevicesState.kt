package com.hermanowicz.wirelesswhisper.navigation.features.scanDevices.state

import com.hermanowicz.wirelesswhisper.data.model.Device

data class ScanDevicesState(
    var showDialogOnPairDeviceConfirmation: Boolean = false,
    var foundDevices: List<Device> = emptyList(),
    var deviceDuringPairing: Device? = null,
    var turnOnDiscoverableMode: Boolean = false,
    var goToPermissionSettings: Boolean = false,
    var showDialogPermissionsScanDevices: Boolean = false,
    var showDialogPermissionsOnDiscoverable: Boolean = false,
    var showDialogPermissionsOnPair: Boolean = false
)
