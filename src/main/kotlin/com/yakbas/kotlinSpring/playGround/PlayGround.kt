package com.yakbas.kotlinSpring.playGround

import com.yakbas.kotlinSpring.encryption.ecc.ECC
import com.yakbas.kotlinSpring.encryption.ecc.Point
import java.math.BigDecimal
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.Provider
import java.security.Security
import java.util.*


fun main() {
    val ecc = ECC(BigDecimal(3), BigDecimal(7))
    val generator = Point(BigDecimal(1), BigDecimal(1))
    println(ecc.doubleAndAdd(100, generator))
   // printProviders()
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
