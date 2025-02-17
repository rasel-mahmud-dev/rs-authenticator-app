package com.rs.rsauthenticator.utils

import android.util.Log
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import org.apache.commons.codec.binary.Base32
import kotlin.experimental.and
import java.nio.ByteBuffer
import java.security.NoSuchAlgorithmException

fun generateTOTP(secret: String, algorithm: String = "SHA1"): String {
    val timeStep = 30L
    val currentTimeSeconds = System.currentTimeMillis() / 1000L
    val counter = currentTimeSeconds / timeStep

    val secretBytes = decodeBase32(secret)
    val counterBytes = ByteBuffer.allocate(8).putLong(counter).array()

    val hmac = hmac(secretBytes, counterBytes, "Hmac$algorithm")
    val offset = hmac[hmac.size - 1] and 0x0F

    val otp = ((hmac[offset.toInt()].toInt() and 0x7F) shl 24
            or ((hmac[offset.toInt() + 1].toInt() and 0xFF) shl 16)
            or ((hmac[offset.toInt() + 2].toInt() and 0xFF) shl 8)
            or (hmac[offset.toInt() + 3].toInt() and 0xFF)) % 1_000_000

    return String.format("%06d", otp)
}

fun decodeBase32(base32: String): ByteArray {
    return Base32().decode(base32)
}

fun hmac(key: ByteArray, data: ByteArray, algorithm: String): ByteArray {
    return try {
        val mac = Mac.getInstance(algorithm)
        mac.init(SecretKeySpec(key, algorithm))
        mac.doFinal(data)
    } catch (e: NoSuchAlgorithmException) {
        throw IllegalArgumentException("Invalid HMAC algorithm: $algorithm")
    }
}

