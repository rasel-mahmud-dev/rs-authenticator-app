package com.rs.rsauthenticator.utils


import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

fun generateTOTP(secret: String): String {
    val timeStep = 30L // 30 seconds time step
    val time = System.currentTimeMillis() / 1000L
    val timeCounter = time / timeStep

    val secretBytes = decodeBase32(secret) // Decoding the base32 secret to byte array
    val counterBytes = ByteArray(8)

    // Fill counter with the 8-byte representation of timeCounter
    for (i in 0 until 8) {
        counterBytes[7 - i] = (timeCounter shr (i * 8) and 0xFF).toByte()
    }

    val hmac = hmacSha256(secretBytes, counterBytes)
    val offset = hmac[hmac.size - 1] and 0x0F
    val otp = ((hmac[offset.toInt()].toInt() and 0x7F) shl 24
            or ((hmac[offset.toInt() + 1].toInt() and 0xFF) shl 16)
            or ((hmac[offset.toInt() + 2].toInt() and 0xFF) shl 8)
            or (hmac[offset.toInt() + 3].toInt() and 0xFF)) % 1000000

    return String.format("%06d", otp)
}

// Function to decode base32 to byte array
fun decodeBase32(base32: String): ByteArray {
    val cleanBase32 = base32.replace("[^A-Z0-9]".toRegex(), "")
    val decoded = ByteArray(cleanBase32.length * 5 / 8)
    var buffer = 0
    var bitsLeft = 0
    var byteIndex = 0

    for (char in cleanBase32) {
        val value = when (char) {
            in 'A'..'Z' -> char.toInt() - 'A'.toInt()
            in '2'..'7' -> char.toInt() - '2'.toInt() + 26
            else -> throw IllegalArgumentException("Invalid Base32 character")
        }

        buffer = (buffer shl 5) or value
        bitsLeft += 5

        if (bitsLeft >= 8) {
            decoded[byteIndex++] = (buffer shr (bitsLeft - 8) and 0xFF).toByte()
            bitsLeft -= 8
        }
    }

    return decoded
}

// Function to perform HMAC-SHA256 hashing
fun hmacSha256(key: ByteArray, data: ByteArray): ByteArray {
    val mac = Mac.getInstance("HmacSHA256")
    val keySpec = SecretKeySpec(key, "HmacSHA256")
    mac.init(keySpec)
    return mac.doFinal(data)
}