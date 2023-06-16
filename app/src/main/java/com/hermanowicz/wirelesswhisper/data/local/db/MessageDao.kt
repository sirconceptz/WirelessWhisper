package com.hermanowicz.wirelesswhisper.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hermanowicz.wirelesswhisper.data.model.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Query("SELECT * FROM message")
    fun observeAll(): Flow<List<MessageEntity>>

    @Query("SELECT * FROM message WHERE senderAddress = :address OR receiverAddress = :address")
    fun observeAllForAddress(address: String): Flow<List<MessageEntity>>

    @Insert
    suspend fun insert(message: MessageEntity)

    @Delete
    suspend fun delete(message: MessageEntity)

}
