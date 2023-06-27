package com.hermanowicz.wirelesswhisper.domain

import com.hermanowicz.wirelesswhisper.di.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveAllChatsUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) : () -> Flow<List<Pair<String, String>>> {
    override fun invoke(): Flow<List<Pair<String, String>>> {
        return messageRepository.observeAllMessages().map { list ->
            list.map { message ->
                if (message.received) {
                    Pair(message.senderAddress, message.senderAddress)
                } else {
                    Pair(message.senderAddress, message.senderAddress) // to change after implement different devices
                }
            }
        }
    }
}
