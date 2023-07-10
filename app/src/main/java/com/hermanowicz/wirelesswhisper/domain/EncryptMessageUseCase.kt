package com.hermanowicz.wirelesswhisper.domain

import com.hermanowicz.wirelesswhisper.data.model.EncryptedMessage
import com.hermanowicz.wirelesswhisper.data.model.Message
import com.hermanowicz.wirelesswhisper.utils.AESFromPassword
import com.hermanowicz.wirelesswhisper.utils.RandomNonce
import com.hermanowicz.wirelesswhisper.utils.Secrets.AES_PASS
import java.nio.ByteBuffer
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

private const val ENCRYPT_ALGO = "AES/GCM/NoPadding"

private const val TAG_LENGTH_BIT = 128

private const val SALT_LENGTH_BYTE = 16

class EncryptMessageUseCase @Inject constructor() : (Message, ByteArray) -> EncryptedMessage {
    override fun invoke(message: Message, iv: ByteArray): EncryptedMessage {
        val salt: ByteArray = RandomNonce.getRandomNonce(SALT_LENGTH_BYTE)

        val aesKeyFromPassword: SecretKey = AESFromPassword.get(AES_PASS.toCharArray(), salt)
        val cipher = Cipher.getInstance(ENCRYPT_ALGO)

        cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword, GCMParameterSpec(TAG_LENGTH_BIT, iv))
        val cipherText = cipher.doFinal(message.message.toByteArray())

        val cipherTextWithIvSalt: ByteArray =
            ByteBuffer.allocate(iv.size + salt.size + cipherText.size)
                .put(iv)
                .put(salt)
                .put(cipherText)
                .array()

        val text = Base64.getEncoder().encodeToString(cipherTextWithIvSalt)
        return EncryptedMessage(
            message = text,
            iv = iv,
            timestamp = message.timestamp,
            senderAddress = message.senderAddress
        )
    }
}
