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
    fun observeByMacAddress(macAddress: String): Flow<Device?>
    fun checkIsExist(macAddress: String): Boolean
    suspend fun insert(device: Device)
    suspend fun updateConnectionStatus(macAddress: String, connectionStatus: Boolean)
    suspend fun updateName(macAddress: String, newName: String)
    suspend fun deleteDevice(macAddress: String)
    suspend fun updateEncryptionKey(address: String, key: ByteArray)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DeviceLocalDataSourceModule {

    @Binds
    abstract fun bindDeviceLocalDataSource(
        deviceLocalDataSourceImpl: DeviceLocalDataSourceImpl
    ): DeviceLocalDataSource
}
