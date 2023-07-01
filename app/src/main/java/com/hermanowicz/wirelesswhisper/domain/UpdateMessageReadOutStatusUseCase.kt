package com.hermanowicz.wirelesswhisper.domain

import com.hermanowicz.wirelesswhisper.di.repository.MessageRepository
import javax.inject.Inject

class UpdateMessageReadOutStatusUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) : suspend (Int, Boolean) -> Unit {
    override suspend fun invoke(messageId: Int, readOut: Boolean) {
        messageRepository.updateMessageReadOutStatus(messageId, readOut)
    }
}
