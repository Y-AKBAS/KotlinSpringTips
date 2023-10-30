package com.yakbas.kotlinSpring.encryption

import org.springframework.stereotype.Service
import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.spec.MGF1ParameterSpec
import javax.crypto.Cipher
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource


@Service
class RSAEncryptionService(private val random: SecureRandom) {

    companion object {
        private const val RSA = "RSA"
        private const val RSA_CIPHER_ALGO = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING"
        private const val MD_NAME = "SHA-256"
        private const val MGF_NAME = "MGF1"

    }

    fun generateKeys(keySize: Int = 2048): RSAKeys {
        val p = BigInteger.probablePrime(keySize, random)
        val q = BigInteger.probablePrime(keySize, random)

        // Euler's totient phi function (p-1)*(q-1)
        val phi = p.minus(BigInteger.ONE).times(q.minus(BigInteger.ONE))

        val publicKey = generatePublicKey(phi)
        val privateKey = publicKey.modInverse(phi)

        return RSAKeys(privateKey = privateKey, publicKey = publicKey, n = p.multiply(q))
    }

    fun generateKeyPair(keySize: Int = 2048): KeyPair {
        val keyPair = KeyPairGenerator.getInstance(RSA)
        keyPair.initialize(keySize, random)
        return keyPair.generateKeyPair()
    }

    fun encrypt(publicKey: BigInteger, n: BigInteger, message: String): BigInteger {
        return BigInteger(message.toByteArray(Charsets.UTF_8)).modPow(publicKey, n)
    }

    fun decrypt(privateKey: BigInteger, n: BigInteger, message: BigInteger): String {
        val decryptedMessage = message.modPow(privateKey, n)
        return String(decryptedMessage.toByteArray(), Charsets.UTF_8)
    }

    fun encrypt(publicKey: PublicKey, message: ByteArray): ByteArray {
        val encryptionCipher: Cipher = Cipher.getInstance(RSA_CIPHER_ALGO)
        val spec = OAEPParameterSpec(
            MD_NAME, MGF_NAME,
            MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT
        )
        encryptionCipher.init(Cipher.ENCRYPT_MODE, publicKey, spec)
        return encryptionCipher.doFinal(message)
    }

    fun decrypt(privateKey: PrivateKey, message: ByteArray): ByteArray {
        val decryptionCipher: Cipher = Cipher.getInstance(RSA_CIPHER_ALGO)
        val spec = OAEPParameterSpec(
            MD_NAME, MGF_NAME,
            MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT
        )
        decryptionCipher.init(Cipher.DECRYPT_MODE, privateKey, spec)
        return decryptionCipher.doFinal(message)
    }

    private fun generatePublicKey(phi: BigInteger): BigInteger {
        val bitLength = phi.bitLength()
        var result = BigInteger(bitLength, random)

        while (!result.gcd(phi).equals(BigInteger.ONE))
            result = BigInteger(bitLength, random)

        return result
    }
}