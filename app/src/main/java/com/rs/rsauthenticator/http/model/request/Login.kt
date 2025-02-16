package com.rs.rsauthenticator.http.model.request


data class LoginApiResponse(
    val message: String?,
    val statusText: String?,
    val data: LoginData?
) {
    data class LoginData(
        val id: String,
        val username: String,
        val email: String,
        val avatar: String?,
        val createdAt: String,
        val status: String,
        val token: String?,
        val sessionId: String?,
        val isRevokedSession: Boolean
    )
}
