package com.hermanowicz.wirelesswhisper.navigation.features.pairedDevices.ui

import androidx.lifecycle.ViewModel
import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.domain.GetPairedDevicesListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PairedDevicesViewModel @Inject constructor(
    private val getPairedDevicesListUseCase: GetPairedDevicesListUseCase
) : ViewModel() {

    val pairedDevices: List<Device> = getPairedDevicesListUseCase()
}
