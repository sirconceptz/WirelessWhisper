package com.hermanowicz.wirelesswhisper.navigation.features.singleChat.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.data.model.Message
import com.hermanowicz.wirelesswhisper.domain.CopyMessageToClipboardUseCase
import com.hermanowicz.wirelesswhisper.domain.DeleteMessageLocallyUseCase
import com.hermanowicz.wirelesswhisper.domain.ObserveDeviceForAddressUseCase
import com.hermanowicz.wirelesswhisper.domain.ObserveMessagesForAddressUseCase
import com.hermanowicz.wirelesswhisper.domain.UpdateMessageReadOutStatusUseCase
import com.hermanowicz.wirelesswhisper.navigation.features.singleChat.state.SingleChatUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingleChatViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val observeMessagesForAddressUseCase: ObserveMessagesForAddressUseCase,
    private val observeDeviceForAddressUseCase: ObserveDeviceForAddressUseCase,
    private val updateMessageReadOutStatusUseCase: UpdateMessageReadOutStatusUseCase,
    private val deleteMessageLocallyUseCase: DeleteMessageLocallyUseCase,
    private val copyMessageToClipboardUseCase: CopyMessageToClipboardUseCase
) : ViewModel() {
    val macAddress: String = savedStateHandle["macAddress"] ?: ""

    private var _state: MutableStateFlow<SingleChatUiState> =
        MutableStateFlow(SingleChatUiState())
    var state: StateFlow<SingleChatUiState> = _state.asStateFlow()

    init {
        observeMessagesForAddress(macAddress)
        observeDeviceForAddress(macAddress)
    }

    private fun observeDeviceForAddress(macAddress: String) {
        if (macAddress.isNotEmpty()) {
            viewModelScope.launch {
                observeDeviceForAddressUseCase(macAddress).collect { device ->
                    updateDeviceForAddress(device)
                }
            }
        }
    }

    private fun updateDeviceForAddress(device: Device) {
        _state.update { it.copy(device = device) }
    }

    private fun observeMessagesForAddress(address: String) {
        if (address.isNotEmpty()) {
            viewModelScope.launch {
                observeMessagesForAddressUseCase(address).collect { messageList ->
                    messageList.forEach {
                        if (!it.readOut) {
                            updateMessageReadOutStatusUseCase(it.id!!, true)
                        }
                    }
                    updateMessageList(messageList)
                }
            }
        }
    }

    private fun updateMessageList(messageList: List<Message>) {
        _state.update { it.copy(messageList = messageList) }
    }

    fun clearCurrentMessage() {
        onCurrentMessageChange("")
    }

    fun onCurrentMessageChange(currentMessage: String) {
        _state.update { it.copy(currentMessage = currentMessage) }
    }

    fun deleteSingleMessage(messageId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteMessageLocallyUseCase(messageId)
        }
    }

    fun onClickEditMode(editMode: Boolean) {
        _state.update { it.copy(deleteMode = editMode) }
    }

    fun copyMessageToClipboard(message: String) {
        copyMessageToClipboardUseCase(message)
    }

    fun onGoToPermissionSettings(bool: Boolean) {
        _state.update {
            it.copy(goToPermissionSettings = bool)
        }
    }

    fun showDialogPermissionsSendMessage(bool: Boolean) {
        _state.update {
            it.copy(showDialogPermissionsSendMessage = bool)
        }
    }
}
