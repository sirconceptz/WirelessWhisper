package com.hermanowicz.wirelesswhisper.domain

import com.hermanowicz.wirelesswhisper.data.model.Message
import javax.inject.Inject

class GetCountedUnreadMessagesUseCase @Inject constructor(
) : (List<Message>) -> Int {
    override fun invoke(messageList: List<Message>): Int {
        return messageList.count { it.readOut }
    }
}