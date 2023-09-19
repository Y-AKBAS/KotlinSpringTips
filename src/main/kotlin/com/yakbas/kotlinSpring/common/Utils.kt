package com.yakbas.kotlinSpring.common

fun <T : Any> T.createLogger(): Logger = LoggerFactory.getLogger(this.javaClass)

fun measureTime(message: String = "", block: () -> Unit) {
    val start = System.currentTimeMillis()
    block()
    println("$message ${System.currentTimeMillis() - start}")
}

