package com.rs.rsauthenticator.http
import com.rs.rsauthenticator.state.AuthState
import com.rs.rsauthenticator.state.Device
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.gson.gson
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            disableHtmlEscaping()
        }
    }

    install(DefaultRequest) {
        val token = AuthState.auth?.token ?: ""
        if (!headers.contains("No-Authentication")) {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
    }
}


data class LoginApiResponse(
    val message: String?,
    val data: LoginData?
) {
    data class LoginData(
        val user: User,
        val session: Session,
        val devices: List<Device>
    ) {
        data class User(
            val _id: String,
            val device: String,
            val username: String,
            val email: String,
            val password: String,
            val avatar: String,
            val createdAt: String,
            val status: String,
            val deviceId: String
        )

        data class Session(
            val _id: String,
            val userId: String,
            val device: String,
            val token: String,
            val createdAt: String
        )
    }
}
