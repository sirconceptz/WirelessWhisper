package com.hermanowicz.wirelesswhisper.domain

import com.hermanowicz.wirelesswhisper.data.model.Message
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val saveMessageLocallyUseCase: SaveMessageLocallyUseCase
) : suspend (Message) -> Unit {
    override suspend fun invoke(message: Message) {
        saveMessageLocallyUseCase(message = message)
        // todo: send message
    }
}
