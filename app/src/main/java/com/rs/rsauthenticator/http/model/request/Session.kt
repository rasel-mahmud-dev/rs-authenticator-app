package com.rs.rsauthenticator.http.model.request

data class Session(
    val _id: String,
    val userId: String,
    val device: String,
    val token: String,
    val createdAt: String
)