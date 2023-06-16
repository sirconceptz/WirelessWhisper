package com.hermanowicz.wirelesswhisper.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hermanowicz.wirelesswhisper.data.model.DeviceEntity
import com.hermanowicz.wirelesswhisper.data.model.MessageEntity

@Database(
    entities = [
        DeviceEntity::class,
        MessageEntity::class
    ],
    version = 1
)
abstract class LocalDb : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao
    abstract fun messageDao(): MessageDao
}
