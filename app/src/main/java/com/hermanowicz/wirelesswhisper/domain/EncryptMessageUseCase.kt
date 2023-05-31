package com.hermanowicz.wirelesswhisper.domain

import com.hermanowicz.wirelesswhisper.data.model.DecryptedMessage
import com.hermanowicz.wirelesswhisper.data.model.EncryptedMessage
import com.hermanowicz.wirelesswhisper.utils.AESFromPassword
import com.hermanowicz.wirelesswhisper.utils.Secrets.AES_PASS
import java.nio.ByteBuffer
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

private const val ENCRYPT_ALGO = "AES/GCM/NoPadding"

private const val TAG_LENGTH_BIT = 128

private const val IV_LENGTH_BYTE = 12
private const val SALT_LENGTH_BYTE = 16

class EncryptMessageUseCase @Inject constructor() : (DecryptedMessage) -> EncryptedMessage {
    override fun invoke(descryptedMessage: DecryptedMessage): EncryptedMessage {
        val salt: ByteArray = getRandomNonce(SALT_LENGTH_BYTE)
        val iv: ByteArray = getRandomNonce(IV_LENGTH_BYTE)

        val aesKeyFromPassword: SecretKey = AESFromPassword.get(AES_PASS.toCharArray(), salt)
        val cipher = Cipher.getInstance(ENCRYPT_ALGO)

        cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword, GCMParameterSpec(TAG_LENGTH_BIT, iv))
        val cipherText = cipher.doFinal(descryptedMessage.message.toByteArray())

        val cipherTextWithIvSalt: ByteArray =
            ByteBuffer.allocate(iv.size + salt.size + cipherText.size)
                .put(iv)
                .put(salt)
                .put(cipherText)
                .array()

        val message = Base64.getEncoder().encodeToString(cipherTextWithIvSalt)
        return EncryptedMessage(
            message = message.toByteArray(),
            iv = iv,
            timestamp = descryptedMessage.timestamp,
            senderMacAddress = descryptedMessage.senderMacAddress
        )
    }

    private fun getRandomNonce(size: Int): ByteArray {
        val nonce = ByteArray(size)
        SecureRandom().nextBytes(nonce)
        return nonce
    }
}
