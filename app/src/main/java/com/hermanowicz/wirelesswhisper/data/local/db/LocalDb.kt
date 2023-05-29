package com.hermanowicz.wirelesswhisper.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hermanowicz.wirelesswhisper.data.model.DeviceEntity

@Database(
    entities = [
        DeviceEntity::class
    ],
    version = 1
)
abstract class LocalDb : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
}
