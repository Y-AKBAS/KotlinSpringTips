package com.yakbas.kotlinSpring.playGround

import com.yakbas.kotlinSpring.encryption.DES
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.Provider
import java.security.Security
import java.util.*


fun main() {
    desEncryption()
}

fun hash(text: String): String {
    val digest = requireNotNull(MessageDigest.getInstance("SHA-256"))
    val hash = requireNotNull(digest.digest(text.toByteArray()))
    return String(hash, StandardCharsets.UTF_8)
}

fun printProviders() {
    Security.getProviders()
        .flatMapTo(TreeSet()) {
            it.services.map(Provider.Service::getAlgorithm)
        }.forEach(::println)
}

fun desEncryption() {
    val des = DES()
    val text = "This is my message you understand?"
    val encryptionResult = des.encrypt(text)
    val decryptionResult = with(encryptionResult) {
        des.decrypt(encryptedBytes, key, ivParameterSpec)
    }
    val decryptionText = String(decryptionResult, StandardCharsets.UTF_8)
    val equal = text == decryptionText
    println(equal)
}
