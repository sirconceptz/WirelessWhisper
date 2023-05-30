package com.hermanowicz.wirelesswhisper.navigation.features.scanDevices.ui

import androidx.lifecycle.ViewModel
import com.hermanowicz.wirelesswhisper.data.model.Device
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScanDevicesViewModel @Inject constructor() : ViewModel() {
    val foundDevices: List<Device> = emptyList()

    fun scanDevices() {
    }
}
