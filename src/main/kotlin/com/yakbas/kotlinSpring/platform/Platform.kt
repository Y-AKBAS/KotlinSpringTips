package com.yakbas.kotlinSpring.platform

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

sealed class Platform {

    object EU : Platform()
    object NA : Platform()

    @Component
    object CURRENT : Platform() {

        private lateinit var platform: Platform

        @Autowired
        fun findPlatform(@Value("\${app.platform}") platformStr: String) {
            platform = if (platformStr == "eu") EU else NA
        }

        override fun name(): String = platform.name()
        fun isEu(): Boolean = platform is EU
        fun isNa(): Boolean = platform is NA
    }

    companion object {
        fun isEu(): Boolean = CURRENT.isEu()
        fun isNa(): Boolean = CURRENT.isNa()
        fun name(): String = CURRENT.name()
    }

    open fun name(): String = this::class.java.simpleName
}
