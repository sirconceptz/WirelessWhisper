package com.hermanowicz.wirelesswhisper.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckIsNotificationsEnabled @Inject constructor() : () -> Flow<Boolean> {
    override fun invoke(): Flow<Boolean> {
        TODO("Not yet implemented")
    }
}