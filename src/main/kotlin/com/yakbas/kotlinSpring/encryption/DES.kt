package com.yakbas.kotlinSpring.encryption

import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class DES() {

    fun encrypt(text: String) = CryptoUtils.encrypt(
        text = text,
        "DES/CBC/PKCS5Padding"
    )

    fun decrypt(bytes: ByteArray, key: SecretKey, initVector: IvParameterSpec) =
        CryptoUtils.decrypt(bytes, "DES/CBC/PKCS5Padding", key, initVector)
}