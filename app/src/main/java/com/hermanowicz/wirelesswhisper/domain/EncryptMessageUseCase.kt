package com.hermanowicz.wirelesswhisper.domain

import com.hermanowicz.wirelesswhisper.data.model.EncryptedMessage
import com.hermanowicz.wirelesswhisper.utils.Secrets.AES_PASS
import java.nio.ByteBuffer
import java.security.SecureRandom
import java.security.spec.KeySpec
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

private const val ENCRYPT_ALGO = "AES/GCM/NoPadding"

private const val TAG_LENGTH_BIT = 128

private const val IV_LENGTH_BYTE = 12
private const val SALT_LENGTH_BYTE = 16

class EncryptMessageUseCase @Inject constructor(

) : (String) -> EncryptedMessage {
    override fun invoke(descrypted: String): EncryptedMessage {

        val salt: ByteArray = getRandomNonce(SALT_LENGTH_BYTE)
        val iv: ByteArray = getRandomNonce(IV_LENGTH_BYTE)

        val aesKeyFromPassword: SecretKey = getAESKeyFromPassword(AES_PASS.toCharArray(), salt)
        val cipher = Cipher.getInstance(ENCRYPT_ALGO)

        cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword, GCMParameterSpec(TAG_LENGTH_BIT, iv))
        val cipherText = cipher.doFinal(descrypted.toByteArray())

        val cipherTextWithIvSalt: ByteArray =
            ByteBuffer.allocate(iv.size + salt.size + cipherText.size)
                .put(iv)
                .put(salt)
                .put(cipherText)
                .array()

        val message = Base64.getEncoder().encodeToString(cipherTextWithIvSalt)
        return EncryptedMessage(message = message.toByteArray(), iv = iv)
    }


    fun getAESKeyFromPassword(password: CharArray?, salt: ByteArray?): SecretKey {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec: KeySpec = PBEKeySpec(password, salt, 65536, 256)
        return SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
    }

    fun getRandomNonce(size: Int): ByteArray {
        val nonce = ByteArray(size)
        SecureRandom().nextBytes(nonce)
        return nonce
    }
}