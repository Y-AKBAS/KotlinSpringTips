package com.yakbas.kotlinSpring

import com.yakbas.kotlinSpring.encryption.AESEncryptionService
import com.yakbas.kotlinSpring.encryption.RSAEncryptionService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.crypto.spec.SecretKeySpec

@SpringBootTest(properties = ["spring.profiles.active=dev"],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KotlinSpringApplicationTests {

    @Autowired
    private lateinit var aesEncryptionService: AESEncryptionService

    @Autowired
    private lateinit var rsaEncryptionService: RSAEncryptionService

    @Test
    fun `that the aes encryption works`() {
        val initialText = "This is my text to encrypt"
        val encryptionResult = aesEncryptionService.encrypt(initialText)
        val decryptedBytes =
            aesEncryptionService.decryptCipherWithIv(encryptionResult.encryptedBytes, encryptionResult.key)
        val decryptedText = String(decryptedBytes, Charsets.UTF_8)

        Assertions.assertEquals(initialText, decryptedText)
    }

    @Test
    fun `that the key can be saved successfully`() {
        val initialText = "This is my text to encrypt"
        val encryptionResult = aesEncryptionService.encrypt(initialText)
        val key = encryptionResult.key.encoded
        val decryptedBytes =
            aesEncryptionService.decryptCipherWithIv(encryptionResult.encryptedBytes, SecretKeySpec(key, "AES"))
        val decryptedText = String(decryptedBytes, Charsets.UTF_8)

        Assertions.assertEquals(initialText, decryptedText)
    }

    @Test
    fun `that the password base encryption works`() {
        val initialText = "This is my text to encrypt"
        val password = "$%gsdgFENDrt!!:,V."
        val iv = aesEncryptionService.generateBytes(AESEncryptionService.IV_LENGTH)
        val salt = aesEncryptionService.generateBytes(AESEncryptionService.SALT_LENGTH)
        val firstKey = aesEncryptionService.generateKeyFromPassword(password, salt)
        val result = aesEncryptionService.encryptAndMergeIv(initialText, firstKey, iv = iv, salt = salt)
        val secondKey = aesEncryptionService.generateKeyFromPassword(password, salt)
        val decryptedBytes = aesEncryptionService.decryptCipherWithIvAndSalt(result, secondKey)
        val decryptedText = String(decryptedBytes, Charsets.UTF_8)
        Assertions.assertEquals(initialText, decryptedText)
    }

    @Test
    fun `that the javax rsa encryption works`() {
        val initialText = "This is my text to encrypt"
        val pair = rsaEncryptionService.generateKeyPair()
        val encryptedText = rsaEncryptionService.encrypt(pair.public, initialText.toByteArray())
        val decryptedBytes = rsaEncryptionService.decrypt(pair.private, encryptedText)
        val decryptedText = String(decryptedBytes, Charsets.UTF_8)
        Assertions.assertEquals(initialText, decryptedText)
    }

    @Test
    fun `that the manual rsa encryption works fine`() {
        val initialText = "This is my text to encrypt"
        val keys = rsaEncryptionService.generateKeys()
        val encryptedInt = rsaEncryptionService.encrypt(keys.publicKey, keys.n, initialText)
        val decryptedText = rsaEncryptionService.decrypt(keys.privateKey, keys.n, encryptedInt)
        Assertions.assertEquals(initialText, decryptedText)
    }

}

