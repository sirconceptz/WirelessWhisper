package com.hermanowicz.wirelesswhisper.data.repository

import com.hermanowicz.wirelesswhisper.data.model.Message
import com.hermanowicz.wirelesswhisper.di.dataSource.MessageLocalDataSource
import com.hermanowicz.wirelesswhisper.di.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageRepositoryImpl @Inject constructor(
    private val messageLocalDataSource: MessageLocalDataSource
) : MessageRepository {
    override fun observeAllMessages(): Flow<List<Message>> {
        return messageLocalDataSource.observeAllMessages()
    }

    override fun observeAllForAddress(address: String): Flow<List<Message>> {
        return messageLocalDataSource.observeAllForAddress(address)
    }

    override suspend fun insertMessage(message: Message) {
        messageLocalDataSource.insertMessage(message)
    }

    override suspend fun deleteMassage(id: Int) {
        messageLocalDataSource.deleteMassage(id)
    }

    override suspend fun updateMessageReadOutStatus(messageId: Int, readOut: Boolean) {
        messageLocalDataSource.updateMessageReadOutStatus(messageId, readOut)
    }
}
