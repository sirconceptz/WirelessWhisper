package com.hermanowicz.wirelesswhisper.navigation.features.singleChat.state

import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.data.model.Message

data class SingleChatUiState(
    var messageList: List<Message> = emptyList(),
    var device: Device? = null,
    var currentMessage: String = "",
    var deleteMode: Boolean = false,
    var goToPermissionSettings: Boolean = false,
    var showDialogPermissionsSendMessage: Boolean = false
)
