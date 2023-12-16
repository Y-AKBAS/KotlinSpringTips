package com.yakbas.kotlinSpring.common

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

fun <T : Any> T.createLogger(): Logger = LoggerFactory.getLogger(this.javaClass)


inline fun <T> getOrNull(get: () -> T?, logError: Boolean = false, loggerName: String? = null): T? = try {
    get()
} catch (ex: Exception) {
    if (logError) {
        val logger = LoggerFactory.getLogger(loggerName ?: "com.yakbas.kotlinSpring.common.Utils")
        logger.warn("", ex)
    }
    null
}

inline fun <T> getOrThrow(get: () -> T?, exSupplier: (Throwable) -> Throwable): T = try {
    checkNotNull(get()) { "Failed get() because of null result" }
} catch (ex: Exception) {
    throw exSupplier(ex)
}

inline fun <T> getOrHandle(get: () -> T?, exHandler: (Throwable) -> Unit): T? = try {
    get()
} catch (ex: Exception) {
    exHandler(ex)
    null
}

inline fun runOrThrow(run: () -> Unit, exSupplier: (Throwable) -> Throwable) = try {
    run()
} catch (ex: Exception) {
    throw exSupplier(ex)
}

inline fun runOrHandle(run: () -> Unit, handler: (Throwable) -> Unit) = try {
    run()
} catch (ex: Exception) {
    handler(ex)
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
