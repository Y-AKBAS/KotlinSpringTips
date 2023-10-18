package com.yakbas.kotlinSpring.common

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

fun <T : Any> T.createLogger(): Logger = LoggerFactory.getLogger(this.javaClass)

fun measureTime(message: String = "", block: () -> Unit) {
    val start = System.currentTimeMillis()
    block()
    println("$message ${System.currentTimeMillis() - start}")
}

inline fun <T> orNull(get: () -> T?, logError: Boolean = false, loggerName: String? = null): T? = try {
    get()
} catch (ex: Exception) {
    if (logError) {
        val logger = LoggerFactory.getLogger(loggerName ?: "com.yakbas.kotlinSpring.common.Utils")
        logger.warn("", ex)
    }
    null
}

fun UUID.toByteArray(): ByteArray {
    val byteArray = ByteArray(16)
    ByteBuffer.wrap(byteArray)
        .order(ByteOrder.BIG_ENDIAN)
        .putLong(this.mostSignificantBits)
        .putLong(this.leastSignificantBits)

    return byteArray
}

fun ByteArray.toUuidString(): String {
    check(this.size == 16) { "Byte array size should be 16" }
    return ByteBuffer.wrap(this).let {
        UUID(it.getLong(), it.getLong())
    }.toString()
}
