package com.hermanowicz.wirelesswhisper.navigation.features.singleChat.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.data.model.Message
import com.hermanowicz.wirelesswhisper.domain.ObserveAllMessagesUseCase
import com.hermanowicz.wirelesswhisper.domain.ObserveDeviceForAddressUseCase
import com.hermanowicz.wirelesswhisper.domain.ObserveMessagesForAddressUseCase
import com.hermanowicz.wirelesswhisper.domain.SendMessageUseCase
import com.hermanowicz.wirelesswhisper.navigation.features.singleChat.state.SingleChatUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val observeAllMessagesUseCase: ObserveAllMessagesUseCase,
    private val observeDeviceForAddressUseCase: ObserveDeviceForAddressUseCase,
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {
    val macAddress: String = savedStateHandle["macAddress"] ?: ""

    private var _uiState: MutableStateFlow<SingleChatUiState> =
        MutableStateFlow(SingleChatUiState())
    var uiState: StateFlow<SingleChatUiState> = _uiState.asStateFlow()

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
        _uiState.update { it.copy(device = device) }
    }

    private fun observeMessagesForAddress(address: String) {
        if (address.isNotEmpty()) {
            viewModelScope.launch {
                observeAllMessagesUseCase().collect { messageList ->
                //observeMessagesForAddressUseCase(address).collect { messageList ->
                    updateMessageList(messageList)
                }
            }
        }
    }

    fun sendMessage() {
        // todo: send message

        clearCurrentMessage()
    }

    private fun updateMessageList(messageList: List<Message>) {
        _uiState.update { it.copy(messageList = messageList) }
    }

    private fun setCurrentMessage(message: String) {
        _uiState.update { it.copy(currentMessage = message) }
    }

    private fun clearCurrentMessage() {
        _uiState.update { it.copy(currentMessage = "") }
    }
}
