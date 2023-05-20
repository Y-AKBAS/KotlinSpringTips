package com.yakbas.kotlinSpring.common

interface Nameable {
    fun name(): String = this::class.java.simpleName
}