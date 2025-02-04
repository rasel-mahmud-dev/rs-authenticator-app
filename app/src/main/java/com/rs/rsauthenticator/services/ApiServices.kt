package com.rs.rsauthenticator.services


import com.rs.rsauthenticator.state.Device
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ApiService {

//    private val client = client // Use the HttpClient from your existing setup
//
//    suspend fun login(username: String, password: String): LoginApiResponse? {
//        return try {
//            val response: HttpResponse = client.post("https://yourapi.com/login") {
//                contentType(ContentType.Application.Json)
//                setBody(mapOf("username" to username, "password" to password))
//            }
//            response.body<LoginApiResponse>()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//    suspend fun register(username: String, email: String, password: String): LoginApiResponse? {
//        return try {
//            val response: HttpResponse = client.post("https://yourapi.com/register") {
//                contentType(ContentType.Application.Json)
//                setBody(mapOf("username" to username, "email" to email, "password" to password))
//            }
//            response.body<LoginApiResponse>()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
}
