package com.yakbas.kotlinSpring.encryption

import javax.crypto.SecretKey

class EncryptionResult(val key: SecretKey, val encryptedBytes: ByteArray)
