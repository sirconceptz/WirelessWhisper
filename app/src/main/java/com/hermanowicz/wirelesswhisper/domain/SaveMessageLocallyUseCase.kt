package com.hermanowicz.wirelesswhisper.domain

import com.hermanowicz.wirelesswhisper.data.model.Message
import com.hermanowicz.wirelesswhisper.di.repository.MessageRepository
import javax.inject.Inject

class SaveMessageLocallyUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) : suspend (Message) -> Unit {
    override suspend fun invoke(message: Message) {
        messageRepository.insertMessage(message)
    }
}
