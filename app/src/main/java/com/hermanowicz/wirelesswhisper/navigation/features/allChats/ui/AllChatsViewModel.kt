package com.hermanowicz.wirelesswhisper.navigation.features.allChats.ui

import androidx.lifecycle.ViewModel
import com.hermanowicz.wirelesswhisper.domain.ObserveAllChatsUseCase
import com.hermanowicz.wirelesswhisper.navigation.features.allChats.state.AllChatsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AllChatsViewModel @Inject constructor(
    private val observeAllChatsUseCase: ObserveAllChatsUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<AllChatsUiState> = MutableStateFlow(AllChatsUiState())
    val uiState: StateFlow<AllChatsUiState> = _uiState.asStateFlow()
}
