package com.hermanowicz.wirelesswhisper.navigation.features.allChats.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.domain.GetAllChatsUseCase
import com.hermanowicz.wirelesswhisper.domain.ObserveAllMessagesUseCase
import com.hermanowicz.wirelesswhisper.domain.ObserveAllPairedDevicesUseCase
import com.hermanowicz.wirelesswhisper.navigation.features.allChats.state.AllChatsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val getAllChatsUseCase: GetAllChatsUseCase
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
                            pairedDevices = connectedDevices,
                            selectedNewMessageDevice = connectedDevices[0].name
                        )
                    }
                }
                getAllChatsUseCase(allMessages, connectedDevices)
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
}
