package com.hermanowicz.wirelesswhisper.data.repository

import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.di.dataSource.DeviceLocalDataSource
import com.hermanowicz.wirelesswhisper.di.repository.DeviceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
    private val localDataSource: DeviceLocalDataSource
) : DeviceRepository {
    override fun observeAll(): Flow<List<Device>> {
        return localDataSource.observeAll()
    }

    override fun observeByMacAddress(macAddress: String): Flow<Device> {
        return localDataSource.observeByMacAddress(macAddress)
    }

    override fun checkIsExist(macAddress: String): Boolean {
        return localDataSource.checkIsExist(macAddress)
    }

    override suspend fun insert(device: Device) {
        localDataSource.insert(device)
    }

    override suspend fun updateName(macAddress: String, newName: String) {
        localDataSource.updateName(macAddress, newName)
    }

    override suspend fun updateConnectionStatus(macAddress: String, connectionStatus: Boolean) {
        localDataSource.updateConnectionStatus(macAddress, connectionStatus)
    }

    override suspend fun deleteDevice(macAddress: String) {
        localDataSource.deleteDevice(macAddress)
    }

    override suspend fun updateEncryptionKey(address: String, key: ByteArray) {
        localDataSource.updateEncryptionKey(address, key)
    }
}
