package com.hermanowicz.wirelesswhisper.domain

import com.hermanowicz.wirelesswhisper.data.model.Message
import com.hermanowicz.wirelesswhisper.di.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class ObserveAllMessagesUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) : () -> Flow<List<Message>> {
    override fun invoke(): Flow<List<Message>> {
        return messageRepository.observeAllMessages().distinctUntilChanged()
    }
}
