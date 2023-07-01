package com.hermanowicz.wirelesswhisper.di.repository

import com.hermanowicz.wirelesswhisper.data.model.Message
import com.hermanowicz.wirelesswhisper.data.repository.MessageRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun observeAllMessages(): Flow<List<Message>>
    fun observeAllForAddress(address: String): Flow<List<Message>>
    suspend fun insertMessage(message: Message)
    suspend fun deleteMassage(id: Int)
    suspend fun updateMessageReadOutStatus(messageId: Int, readOut: Boolean)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class MessageLocalDataSourceModule {

    @Binds
    abstract fun bindDeviceLocalDataSource(
        messageRepositoryImpl: MessageRepositoryImpl
    ): MessageRepository
}
