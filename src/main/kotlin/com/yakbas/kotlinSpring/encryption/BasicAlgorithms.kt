package com.yakbas.kotlinSpring.encryption

class CaesarEncryption(private val shiftKey: Int) {

    fun encrypt(text: String): String {
        val encryptedText = text.map {
            val encryptedChar = (it.code + shiftKey) % Char.MAX_VALUE.code
            encryptedChar.toChar()
        }.joinToString(separator = "")

        return encryptedText
    }

    fun decrypt(text: String): String {
        val decryptedText = text.map {
            val decryptedChar = (it.code - shiftKey) % Char.MAX_VALUE.code
            decryptedChar.toChar()
        }.joinToString(separator = "")

        return decryptedText
    }
}