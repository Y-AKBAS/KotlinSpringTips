package com.yakbas.kotlinSpring.encryption

import org.springframework.stereotype.Service
import java.nio.ByteBuffer
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


/*

* Don’t reuse IV with the same key.
* It is ok for IV to be publicly known, the only secret is the key, keep it private and confidential.

 */

@Service
class AESEncryptionService(private val secureRandom: SecureRandom) {

    companion object {
        private const val ALGO = "AES"
        private const val CIPHER_ALGO = "$ALGO/GCM/NoPadding"
        private const val SECRET_KEY_FACTORY_ALGO = "PBKDF2WithHmacSHA256"
        private const val DEFAULT_KEY_SIZE = 256 // Bit
        const val IV_LENGTH = 12 // byte
        const val SALT_LENGTH = 16 // byte
        private const val TAG_SIZE = 128 // Bit
        private const val ITERATION_COUNT = 65536
    }

    fun encrypt(text: String): EncryptionResult {
        val key = generateKey()
        val iv = generateBytes(IV_LENGTH)
        val cipher = encryptAndMergeIv(text, key, iv)
        return EncryptionResult(key, cipher)
    }

    fun encrypt(text: String, key: SecretKey, iv: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(CIPHER_ALGO).apply {
            init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(TAG_SIZE, iv))
        }

        return cipher.doFinal(text.toByteArray(Charsets.UTF_8))
    }

    fun encryptAndMergeIv(text: String, key: SecretKey, iv: ByteArray): ByteArray {
        val encryptedBytes = encrypt(text, key, iv)
        return ByteBuffer.allocate(encryptedBytes.size + iv.size)
            .put(iv)
            .put(encryptedBytes)
            .array()
    }

    fun encryptAndMergeIv(text: String, key: SecretKey, iv: ByteArray, salt: ByteArray): ByteArray {
        val encryptedBytes = encrypt(text, key, iv)
        return ByteBuffer.allocate(encryptedBytes.size + iv.size + salt.size)
            .put(iv)
            .put(salt)
            .put(encryptedBytes)
            .array()
    }

    fun decrypt(cipherText: ByteArray, key: SecretKey, iv: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(CIPHER_ALGO).apply {
            init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(TAG_SIZE, iv))
        }
        return cipher.doFinal(cipherText)
    }

    fun decryptCipherWithIv(cipherText: ByteArray, key: SecretKey): ByteArray {
        val buffer = ByteBuffer.wrap(cipherText)
        val iv = ByteArray(IV_LENGTH).apply { buffer.get(this) }
        val cipher = ByteArray(buffer.remaining()).apply { buffer.get(this) }

        return decrypt(cipher, key, iv)
    }

    fun decryptCipherWithIvAndSalt(cipherText: ByteArray, key: SecretKey): ByteArray {
        val buffer = ByteBuffer.wrap(cipherText)
        val iv = ByteArray(IV_LENGTH).apply { buffer.get(this) }
        val salt = ByteArray(SALT_LENGTH).apply { buffer.get(this) }
        val cipher = ByteArray(buffer.remaining()).apply { buffer.get(this) }

        return decrypt(cipher, key, iv)
    }

    fun generateKey(keySize: Int = DEFAULT_KEY_SIZE): SecretKey {
        return KeyGenerator.getInstance(ALGO).apply {
            this.init(keySize, secureRandom)
        }.generateKey()
    }

    fun generateKeyFromPassword(password: String, salt: ByteArray): SecretKey {
        val keyFactory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGO)
        val keySpec = PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, DEFAULT_KEY_SIZE)
        return SecretKeySpec(keyFactory.generateSecret(keySpec).encoded, ALGO)
    }

    fun generateBytes(size: Int) = ByteArray(size).apply { secureRandom.nextBytes(this) }
}