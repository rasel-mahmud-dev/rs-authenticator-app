package com.rs.rsauthenticator.http.model.request


data class RegistrationApiResponse(
    val message: String?,
    val statusCode: String?,
    val data: RegistrationData?
) {
    data class RegistrationData(
        val id: String,
        val username: String,
        val email: String,
        val password: String?,
        val avatar: String?,
        val createdAt: String?
    )
}