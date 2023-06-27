package com.hermanowicz.wirelesswhisper.domain

import com.hermanowicz.wirelesswhisper.di.repository.MessageRepository
import javax.inject.Inject

class DeleteMessageLocallyUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) : suspend (Int) -> Unit {
    override suspend fun invoke(id: Int) {
        messageRepository.deleteMassage(id)
    }
}
