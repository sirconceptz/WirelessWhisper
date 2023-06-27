package com.hermanowicz.wirelesswhisper.domain

import android.bluetooth.BluetoothManager
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GetDeviceAddressUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) : () -> String {
    override fun invoke(): String {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val btAdapter = bluetoothManager.adapter
        return btAdapter.address
    }
}
