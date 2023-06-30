package com.hermanowicz.wirelesswhisper.navigation.features.allChats.state

import com.hermanowicz.wirelesswhisper.data.model.Device

data class AllChatsUiState(
    var chatList: List<Pair<String, String>> = emptyList(),
    var pairedDevices: List<Device> = emptyList(),
    var connectedDevices: List<Device> = emptyList(),
    var showDialogNewMessage: Boolean = false,
    var showDropdownNewMessage: Boolean = false,
    var newMessageDevice: Device? = null
)
