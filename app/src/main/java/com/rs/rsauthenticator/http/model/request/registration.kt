package com.rs.rsauthenticator.http.model.request


data class RegistrationApiResponse(
    val message: String?,
    val user: RegistrationData?
) {
    data class RegistrationData(
        val _id: String,
        val username: String,
        val email: String,
        val password: String?,
        val avatar: String?,
        val status: String?,
        val createdAt: String?
    )
}