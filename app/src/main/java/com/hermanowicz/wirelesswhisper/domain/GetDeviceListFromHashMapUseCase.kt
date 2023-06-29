package com.hermanowicz.wirelesswhisper.domain

import android.bluetooth.BluetoothDevice
import android.content.Context
import com.hermanowicz.wirelesswhisper.R
import com.hermanowicz.wirelesswhisper.data.model.Device
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class GetDeviceListFromHashMapUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) : () -> List<Device> {

    private var devicesMap = hashSetOf<Device>()

    override fun invoke(): List<Device> {
        return devicesMap.toList()
    }

    fun addDevice(btDevice: BluetoothDevice) {
        try {
            val name = btDevice.name ?: context.getString(R.string.unnamed)
            val address = btDevice.address
            val newDevice = Device(macAddress = address, name = name)
            if (!devicesMap.contains(newDevice)) {
                devicesMap.add(newDevice)
            }
        } catch (e: SecurityException) {
            Timber.e(e.message)
        }
    }

    fun clearFoundDevices() {
        devicesMap = hashSetOf()
    }
}
