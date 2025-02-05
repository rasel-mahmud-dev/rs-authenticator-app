package com.rs.rsauthenticator.dto

data class AuthenticatorEntry(
    val id: String,
    val issuer: String,
    val logoUrl: String,
    val accountName: String,
    val secret: String,
    val otpCode: String,
    val remainingTime: Float // Countdown progress (0 to 1)
)
