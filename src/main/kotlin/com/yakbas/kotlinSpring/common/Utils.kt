package com.yakbas.kotlinSpring.common


fun measureTime(message: String = "", block: () -> Unit) {
    val start = System.currentTimeMillis()
    block()
    println("$message ${System.currentTimeMillis() - start}")
}

