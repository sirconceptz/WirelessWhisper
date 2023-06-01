package com.hermanowicz.wirelesswhisper.di.dataSource

import com.hermanowicz.wirelesswhisper.data.dataSource.DeviceLocalDataSourceImpl
import com.hermanowicz.wirelesswhisper.data.model.Device
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow

interface DeviceLocalDataSource {
    fun observeAll(): Flow<List<Device>>
    fun observeByMacAddress(macAddress: String): Flow<Device>
    suspend fun updateName(macAddress: String, newName: String)
    suspend fun deleteDevice(device: Device)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DeviceLocalDataSourceModule {

    @Binds
    abstract fun bindDeviceLocalDataSource(
        deviceLocalDataSourceImpl: DeviceLocalDataSourceImpl
    ): DeviceLocalDataSource
}
