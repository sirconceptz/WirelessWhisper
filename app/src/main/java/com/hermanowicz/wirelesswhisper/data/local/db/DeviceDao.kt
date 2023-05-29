package com.hermanowicz.wirelesswhisper.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hermanowicz.wirelesswhisper.data.model.DeviceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {

    @Query("SELECT * FROM device")
    fun observeAll(): Flow<List<DeviceEntity>>

    @Query("SELECT * FROM device WHERE macAddress = (:macAddress)")
    fun observeByAddress(macAddress: String): Flow<DeviceEntity>

    @Insert
    fun insert(device: DeviceEntity)

    @Delete
    fun delete(device: DeviceEntity)
}