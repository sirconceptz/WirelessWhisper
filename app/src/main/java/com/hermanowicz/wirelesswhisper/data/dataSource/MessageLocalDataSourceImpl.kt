package com.hermanowicz.wirelesswhisper.data.dataSource

import com.hermanowicz.wirelesswhisper.data.local.db.MessageDao
import com.hermanowicz.wirelesswhisper.data.mapper.toDomainModel
import com.hermanowicz.wirelesswhisper.data.mapper.toEntityModel
import com.hermanowicz.wirelesswhisper.data.model.Message
import com.hermanowicz.wirelesswhisper.di.dataSource.MessageLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MessageLocalDataSourceImpl @Inject constructor(
    private val messageDao: MessageDao
) : MessageLocalDataSource {
    override fun observeAllMessages(): Flow<List<Message>> {
        return messageDao.observeAll().map { list -> list.map { it.toDomainModel() } }
    }

    override fun observeAllForAddress(address: String): Flow<List<Message>> {
        return messageDao.observeAllForAddress(address)
            .map { list -> list.map { it.toDomainModel() } }
    }

    override suspend fun insertMessage(message: Message) {
        messageDao.insert(message.toEntityModel())
    }

    override suspend fun deleteMassage(id: Int) {
        messageDao.delete(id)
    }

    override suspend fun updateMessageReadOutStatus(messageId: Int, readOut: Boolean) {
        messageDao.updateMessageReadOutStatus(messageId, readOut)
    }
}
