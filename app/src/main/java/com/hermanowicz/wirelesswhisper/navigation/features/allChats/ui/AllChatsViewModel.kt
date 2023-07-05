package com.hermanowicz.wirelesswhisper.navigation.features.allChats.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.domain.DeleteMessageLocallyUseCase
import com.hermanowicz.wirelesswhisper.domain.GetAllChatsUseCase
import com.hermanowicz.wirelesswhisper.domain.ObserveAllMessagesUseCase
import com.hermanowicz.wirelesswhisper.domain.ObserveAllPairedDevicesUseCase
import com.hermanowicz.wirelesswhisper.domain.ObserveMessagesForAddressUseCase
import com.hermanowicz.wirelesswhisper.navigation.features.allChats.state.AllChatsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllChatsViewModel @Inject constructor(
    private val observeAllMessagesUseCase: ObserveAllMessagesUseCase,
    private val observeAllPairedDevicesUseCase: ObserveAllPairedDevicesUseCase,
    private val getAllChatsUseCase: GetAllChatsUseCase,
    private val observeMessagesForAddressUseCase: ObserveMessagesForAddressUseCase,
    private val deleteMessageLocallyUseCase: DeleteMessageLocallyUseCase
) : ViewModel() {
    private val _uiState: MutableStateFlow<AllChatsUiState> = MutableStateFlow(AllChatsUiState())

    val uiState: StateFlow<AllChatsUiState> = _uiState.asStateFlow()

    init {
        observeAllChats()
    }

    private fun observeAllChats() {
        viewModelScope.launch {
            observeAllMessagesUseCase().combine(observeAllPairedDevicesUseCase()) { allMessages, pairedDevices ->
                val connectedDevices = pairedDevices.filter { it.connected }
                if (connectedDevices.isNotEmpty()) {
                    _uiState.update {
                        it.copy(
                            pairedDevices = pairedDevices,
                            connectedDevices = connectedDevices,
                            newMessageDevice = connectedDevices[0]
                        )
                    }
                }
                getAllChatsUseCase(allMessages, pairedDevices)
            }.collect { chatList ->
                _uiState.update { it.copy(chatList = chatList) }
            }
        }
    }

    fun showDialogNewMessage(show: Boolean) {
        _uiState.update { it.copy(showDialogNewMessage = show) }
    }

    fun showNewMessageDropdown(show: Boolean) {
        _uiState.update { it.copy(showDropdownNewMessage = show) }
    }

    fun onSelectNewMessageDevice(device: Device?) {
        _uiState.update { it.copy(newMessageDevice = device) }
    }

    fun deleteSingleChat(address: String) {
        viewModelScope.launch(Dispatchers.IO) {
            observeMessagesForAddressUseCase(address).collect { messageList ->
                messageList.forEach { message ->
                    message.id?.let { deleteMessageLocallyUseCase(it) }
                }
            }
        }
    }
}
