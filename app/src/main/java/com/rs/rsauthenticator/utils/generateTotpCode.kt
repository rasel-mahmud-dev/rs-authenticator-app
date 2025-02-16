package com.rs.rsauthenticator.utils

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import org.apache.commons.codec.binary.Base32
import kotlin.experimental.and
import java.nio.ByteBuffer

fun generateTOTP(secret: String): String {
    val timeStep = 30L
    val currentTimeSeconds = System.currentTimeMillis() / 1000L
    val counter = currentTimeSeconds / timeStep

    val secretBytes = decodeBase32(secret)
    val counterBytes = ByteBuffer.allocate(8).putLong(counter).array()

    val hmac = hmacSha256(secretBytes, counterBytes)
    val offset = hmac[hmac.size - 1] and 0x0F

    val otp = ((hmac[offset.toInt()].toInt() and 0x7F) shl 24
            or ((hmac[offset.toInt() + 1].toInt() and 0xFF) shl 16)
            or ((hmac[offset.toInt() + 2].toInt() and 0xFF) shl 8)
            or (hmac[offset.toInt() + 3].toInt() and 0xFF)) % 1_000_000

    return String.format("%06d", otp)
}

fun decodeBase32(base32: String): ByteArray {
    val base32Codec = Base32()
    return base32Codec.decode(base32)
}

fun hmacSha256(key: ByteArray, data: ByteArray): ByteArray {
    val mac = Mac.getInstance("HmacSHA256")
    val keySpec = SecretKeySpec(key, "HmacSHA256")
    mac.init(keySpec)
    return mac.doFinal(data)
}

