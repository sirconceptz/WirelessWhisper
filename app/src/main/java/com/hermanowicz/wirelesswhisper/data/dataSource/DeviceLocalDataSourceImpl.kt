package com.hermanowicz.wirelesswhisper.data.dataSource

import com.hermanowicz.wirelesswhisper.data.local.db.DeviceDao
import com.hermanowicz.wirelesswhisper.data.mapper.toDomainModel
import com.hermanowicz.wirelesswhisper.data.mapper.toEntityModel
import com.hermanowicz.wirelesswhisper.data.model.Device
import com.hermanowicz.wirelesswhisper.di.dataSource.DeviceLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DeviceLocalDataSourceImpl @Inject constructor(
    private val dao: DeviceDao
) : DeviceLocalDataSource {
    override fun observeAll(): Flow<List<Device>> {
        return dao.observeAll().map { devices -> devices.map { it.toDomainModel() } }
    }

    override fun observeByMacAddress(macAddress: String): Flow<Device> {
        return dao.observeByAddress(macAddress).map { it.toDomainModel() }
    }

    override suspend fun updateName(macAddress: String, newName: String) {
        dao.updateName(macAddress, newName)
    }

    override suspend fun deleteDevice(device: Device) {
        dao.delete(device.toEntityModel())
    }
}
