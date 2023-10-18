package com.yakbas.kotlinSpring.encryption

import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

object CryptoUtils {

    // secretKey.encoded gives us the byte array of the secret key

    private val random = SecureRandom()

    fun getInitVector(cipher: Cipher) = getInitVector(cipher.blockSize)
    fun getInitVector(size: Int) = generateBytes(size).let(::IvParameterSpec)
    fun generateKey(algorithm: String) = KeyGenerator.getInstance(algorithm).generateKey()
    fun generateKeyFromCipherAlgo(algorithm: String): SecretKey {
        return if (algorithm.contains('/')) {
            generateKey(algorithm.takeWhile { it != '/' })
        } else generateKey(algorithm)
    }

    fun generateBytes(size: Int) = ByteArray(size).apply { random.nextBytes(this) }
    fun encodeToString(secretKey: SecretKey) = Base64.getEncoder().encodeToString(secretKey.encoded)
    fun encrypt(
        text: String,
        cipherAlgorithm: String,
        key: SecretKey = generateKeyFromCipherAlgo(cipherAlgorithm),
        ivParameterSpec: IvParameterSpec? = null
    ): EncryptionResult {
        val cipher: Cipher = Cipher.getInstance(cipherAlgorithm)
        val initVector = ivParameterSpec ?: getInitVector(cipher)
        cipher.init(Cipher.ENCRYPT_MODE, key, initVector)
        val encryptedBytes: ByteArray = cipher.doFinal(text.toByteArray())
        return EncryptionResult(key, initVector, encryptedBytes)
    }

    fun decrypt(bytes: ByteArray, cipherAlgorithm: String, key: SecretKey, ivParameterSpec: IvParameterSpec) =
        Cipher.getInstance(cipherAlgorithm).apply {
            this.init(Cipher.DECRYPT_MODE, key, ivParameterSpec)
        }.doFinal(bytes)
}