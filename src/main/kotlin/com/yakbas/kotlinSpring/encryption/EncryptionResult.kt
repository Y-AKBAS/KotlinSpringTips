package com.yakbas.kotlinSpring.encryption

import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class EncryptionResult(val key: SecretKey, val ivParameterSpec: IvParameterSpec, val encryptedBytes: ByteArray)
