package com.hermanowicz.wirelesswhisper.utils

import java.security.SecureRandom

class RandomNonce {
    companion object {
        fun getRandomNonce(size: Int): ByteArray {
            val nonce = ByteArray(size)
            val x = SecureRandom()
            x.nextBytes(nonce)
            return nonce
        }
    }
}
