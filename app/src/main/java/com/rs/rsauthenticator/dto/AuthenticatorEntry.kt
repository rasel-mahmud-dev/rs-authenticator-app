package com.rs.rsauthenticator.dto

data class AuthenticatorEntry(
    val id: String,
    val issuer: String,
    val logoUrl: String,
    val accountName: String,
    val secret: String,
    var otpCode: String,
    val remainingTime: Long,
    val algorithm: String,
    val createdAt: Long
)
