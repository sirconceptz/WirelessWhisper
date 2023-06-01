package com.hermanowicz.wirelesswhisper.di.repository

import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.data.repository.DeviceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    fun observeAll(): Flow<List<Device>>
    fun observeByMacAddress(macAddress: String): Flow<Device>
    fun checkIsExist(macAddress: String): Boolean
    suspend fun insert(device: Device)
    suspend fun updateName(macAddress: String, newName: String)
    suspend fun deleteDevice(macAddress: String)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DeviceLocalDataSourceModule {

    @Binds
    abstract fun bindDeviceRepository(
        deviceRepositoryImpl: DeviceRepositoryImpl
    ): DeviceRepository
}
