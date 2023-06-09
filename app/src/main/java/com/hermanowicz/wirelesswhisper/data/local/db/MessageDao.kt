package com.hermanowicz.wirelesswhisper.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hermanowicz.wirelesswhisper.data.model.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Query("SELECT * FROM message")
    fun observeAll(): Flow<List<MessageEntity>>

    @Query("SELECT * FROM message WHERE senderAddress = :address OR receiverAddress = :address")
    fun observeAllForAddress(address: String): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: MessageEntity)

    @Query("UPDATE message SET readOut = :readOut WHERE id IN (:messageId)")
    suspend fun updateMessageReadOutStatus(messageId: Int, readOut: Boolean)

    @Query("DELETE FROM message WHERE id IN (:messageId)")
    suspend fun delete(messageId: Int)
}
