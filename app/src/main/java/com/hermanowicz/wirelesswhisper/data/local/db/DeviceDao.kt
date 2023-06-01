package com.hermanowicz.wirelesswhisper.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hermanowicz.wirelesswhisper.data.model.DeviceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {

    @Query("SELECT * FROM device")
    fun observeAll(): Flow<List<DeviceEntity>>

    @Query("SELECT * FROM device WHERE macAddress = (:macAddress)")
    fun observeByAddress(macAddress: String): Flow<DeviceEntity>

    @Query("SELECT EXISTS(SELECT * FROM device WHERE macAddress = :macAddress)")
    fun isExist(macAddress: String): Boolean

    @Query("UPDATE device SET name = :newName WHERE macAddress IN (:macAddress)")
    suspend fun updateName(macAddress: String, newName: String)

    @Insert
    suspend fun insert(device: DeviceEntity)

    @Query("DELETE FROM device WHERE macAddress IN (:macAddress)")
    suspend fun delete(macAddress: String)
}
