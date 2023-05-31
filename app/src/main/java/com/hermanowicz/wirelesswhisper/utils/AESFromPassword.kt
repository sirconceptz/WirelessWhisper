package com.hermanowicz.wirelesswhisper.utils

import java.security.spec.KeySpec
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class AESFromPassword() {
    companion object {
        fun get(password: CharArray?, salt: ByteArray?): SecretKey {
            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            val spec: KeySpec = PBEKeySpec(password, salt, 65536, 256)
            return SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
        }
    }
}
