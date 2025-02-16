package com.rs.rsauthenticator.http.services


import com.rs.rsauthenticator.http.HttpClient
import com.rs.rsauthenticator.http.model.request.LoginApiResponse
import com.rs.rsauthenticator.http.model.request.RegistrationApiResponse
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*

object ApiService {

    private val client = HttpClient

    suspend fun login(username: String, password: String): LoginApiResponse? {
        return try {
            val response: HttpResponse = client.post("/api/v1/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("email" to username, "password" to password))
            }
            response.body<LoginApiResponse>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun register(
        username: String,
        email: String,
        password: String
    ): RegistrationApiResponse? {
        return try {
            val response: HttpResponse = client.post("/api/v1/auth/registration") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("username" to username, "email" to email, "password" to password))
            }
            println(response.bodyAsText())
            response.body<RegistrationApiResponse>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
