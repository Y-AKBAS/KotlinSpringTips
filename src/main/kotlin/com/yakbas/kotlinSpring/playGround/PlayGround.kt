package com.yakbas.kotlinSpring.playGround

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.Provider
import java.security.Security
import java.util.*


fun main() {
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

