package com.hermanowicz.wirelesswhisper.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hermanowicz.wirelesswhisper.data.model.MessageEntity
import com.hermanowicz.wirelesswhisper.utils.enums.MessageStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Query("SELECT * FROM message")
    fun observeAll(): Flow<List<MessageEntity>>

    @Query("SELECT * FROM message WHERE senderAddress = :address OR receiverAddress = :address")
    fun observeAllForAddress(address: String): Flow<List<MessageEntity>>

    @Insert
    suspend fun insert(message: MessageEntity)

    @Query("UPDATE message SET messageStatus = :messageStatus WHERE id IN (:messageId)")
    suspend fun updateMessageStatus(messageId: Int, messageStatus: MessageStatus)

    @Query("DELETE FROM message WHERE id IN (:messageId)")
    suspend fun delete(messageId: Int)
}
