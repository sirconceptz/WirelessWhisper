package com.hermanowicz.wirelesswhisper.domain

import android.bluetooth.BluetoothManager
import android.content.Context
import com.hermanowicz.wirelesswhisper.data.model.Device
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class GetPairedDevicesListUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) : () -> List<Device> {
    override fun invoke(): List<Device> {
        return try {
            val mutableDeviceList = mutableListOf<Device>()
            val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            val btAdapter = bluetoothManager.adapter
            val bondedDevices = btAdapter.bondedDevices
            bondedDevices.forEach {
                mutableDeviceList.add(Device(address = it.address, name = it.name))
            }
            mutableDeviceList.toList()
        } catch (e: SecurityException) {
            Timber.e("BT Adapter - bonded devices: $e")
            emptyList()
        }
    }
}
