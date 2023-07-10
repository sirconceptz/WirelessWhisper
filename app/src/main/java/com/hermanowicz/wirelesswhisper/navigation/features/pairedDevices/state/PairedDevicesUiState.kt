package com.hermanowicz.wirelesswhisper.navigation.features.pairedDevices.state

import com.hermanowicz.wirelesswhisper.data.model.Device

data class PairedDevicesUiState(
    val deviceList: List<Device> = emptyList()
)
