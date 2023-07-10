package com.hermanowicz.wirelesswhisper.domain

import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import com.hermanowicz.wirelesswhisper.di.repository.DeviceRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpdateDevicesConnectionStatusUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val deviceRepository: DeviceRepository
) : () -> Unit {

    private val job by lazy { SupervisorJob() }
    private val scope by lazy { CoroutineScope(Dispatchers.IO + job) }

    override fun invoke() {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        scope.launch {
            deviceRepository.observeAll().collect { deviceList ->
                deviceList.forEach {
                    val device = bluetoothAdapter.getRemoteDevice(it.macAddress)
                }
            }
        }
    }
}
