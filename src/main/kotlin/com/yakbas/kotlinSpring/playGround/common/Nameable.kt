package com.yakbas.kotlinSpring.playGround.common

interface Nameable {
    fun name(): String = this::class.java.simpleName
}