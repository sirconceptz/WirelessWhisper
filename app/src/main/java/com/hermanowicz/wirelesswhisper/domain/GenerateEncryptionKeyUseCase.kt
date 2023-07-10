package com.hermanowicz.wirelesswhisper.domain

import com.hermanowicz.wirelesswhisper.utils.RandomNonce
import javax.inject.Inject

class GenerateEncryptionKeyUseCase @Inject constructor() : () -> ByteArray {
    override fun invoke(): ByteArray {
        return RandomNonce.getRandomNonce(12).toString().toByteArray()
    }
}
