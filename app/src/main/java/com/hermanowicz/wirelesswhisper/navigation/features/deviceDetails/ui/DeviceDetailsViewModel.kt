package com.hermanowicz.wirelesswhisper.navigation.features.deviceDetails.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeviceDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val macAddress: String = savedStateHandle["macAddress"] ?: ""
}