package com.yakbas.kotlinSpring.encryption

import org.springframework.stereotype.Service
import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom

@Service
class RSAEncryptionService(private val random: SecureRandom) {

    companion object {
        private const val RSA = "RSA"
    }

    fun generateKeys(keyDigits: Int = 2048): RSAKeys {
        val p = BigInteger.probablePrime(keyDigits, random)
        val q = BigInteger.probablePrime(keyDigits, random)

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

    private fun generatePublicKey(phi: BigInteger): BigInteger {
        val bitLength = phi.bitLength()
        var result = BigInteger(bitLength, random)

        while (!result.gcd(phi).equals(BigInteger.ONE))
            result = BigInteger(bitLength, random)

        return result
    }
}