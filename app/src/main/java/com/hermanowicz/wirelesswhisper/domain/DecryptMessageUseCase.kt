package com.hermanowicz.wirelesswhisper.domain

import com.hermanowicz.wirelesswhisper.data.model.DecryptedMessage
import com.hermanowicz.wirelesswhisper.data.model.EncryptedMessage
import com.hermanowicz.wirelesswhisper.utils.AESFromPassword
import com.hermanowicz.wirelesswhisper.utils.Secrets.AES_PASS
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

private const val ENCRYPT_ALGO = "AES/GCM/NoPadding"

private const val TAG_LENGTH_BIT = 128

private const val IV_LENGTH_BYTE = 12
private const val SALT_LENGTH_BYTE = 16

class DecryptMessageUseCase @Inject constructor() : (EncryptedMessage) -> DecryptedMessage {
    override fun invoke(encryptedMessage: EncryptedMessage): DecryptedMessage {
        val decode: ByteArray = Base64.getDecoder().decode(encryptedMessage.message)

        val bb: ByteBuffer = ByteBuffer.wrap(decode)
        val iv = ByteArray(IV_LENGTH_BYTE)
        bb.get(iv)
        val salt = ByteArray(SALT_LENGTH_BYTE)
        bb.get(salt)
        val cipherText = ByteArray(bb.remaining())
        bb.get(cipherText)

        val aesKeyFromPassword: SecretKey = AESFromPassword.get(AES_PASS.toCharArray(), salt)
        val cipher = Cipher.getInstance(ENCRYPT_ALGO)
        cipher.init(Cipher.DECRYPT_MODE, aesKeyFromPassword, GCMParameterSpec(TAG_LENGTH_BIT, iv))
        val plainText = cipher.doFinal(cipherText)
        return DecryptedMessage(
            message = String(plainText, StandardCharsets.UTF_8),
            timestamp = encryptedMessage.timestamp,
            senderMacAddress = encryptedMessage.senderMacAddress
        )
    }
}
