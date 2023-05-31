package com.hermanowicz.wirelesswhisper.navigation.features.scanDevices.state

import com.hermanowicz.wirelesswhisper.data.model.Device

data class ScanDevicesState(
    var showDialogOnPairDevice: Boolean = false,
    var foundDevices: List<Device> = emptyList(),
    var deviceDuringPairing: Device? = null

)
