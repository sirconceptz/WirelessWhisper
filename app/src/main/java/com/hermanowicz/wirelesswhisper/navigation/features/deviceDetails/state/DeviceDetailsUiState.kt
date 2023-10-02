package com.hermanowicz.wirelesswhisper.navigation.features.deviceDetails.state

import com.hermanowicz.wirelesswhisper.data.model.Device

data class DeviceDetailsUiState(
    var device: Device? = null,
    var goToPermissionSettings: Boolean = false,
    var showDialogPermissionsConnectDevice: Boolean = false,
    var showDialogPermissionsDisconnectDevice: Boolean = false
)
