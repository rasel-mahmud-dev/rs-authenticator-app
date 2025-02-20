package com.rs.rsauthenticator.http
import com.rs.rsauthenticator.apis.BASE_URL
import com.rs.rsauthenticator.state.AppState
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.gson.gson
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders

val HttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            disableHtmlEscaping()
        }
    }

    install(DefaultRequest) {
        val token = AppState.auth?.token ?: ""
        if (!headers.contains("No-Authentication")) {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        url(BASE_URL)
    }
}

