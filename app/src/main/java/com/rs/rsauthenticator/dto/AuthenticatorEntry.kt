package com.rs.rsauthenticator.dto

import com.rs.rsauthenticator.utils.extractOwnerAndSiteLogo
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

data class AuthenticatorEntry(
    val id: String,
    val protocol: String,
    val type: String,
    val accountName: String,
    val secret: String,
    val issuer: String,
    val algorithm: String,
    val digits: Int,
    val period: Int,
    val logoUrl: String?,
    var newOtp: String,
    val remainingTime: Float?,
    val createdAt: Long
) {
    companion object {
        fun fromUri(uri: String): AuthenticatorEntry? {
            return try {
                val decodedUri = URLDecoder.decode(uri, StandardCharsets.UTF_8.name())

                val regex = Regex("otpauth://(\\w+)/(.*)\\?(.*)")
                val matchResult = regex.find(decodedUri) ?: return null

                val type = matchResult.groupValues[1]
                val queryParams = matchResult.groupValues[3]

                val params = queryParams.split("&").associate {
                    val (key, value) = it.split("=")
                    key to value
                }


                val issuer = params["issuer"]

                val (ownerEmail, siteLogo) = extractOwnerAndSiteLogo(uri)

                val algorithm = params["algorithm"] ?: "SHA1"

                AuthenticatorEntry(
                    id = "",
                    protocol = "otpauth",
                    type = type,
                    accountName = ownerEmail,
                    secret = params["secret"] ?: "",
                    issuer = issuer ?: "",
                    algorithm = algorithm,
                    digits = params["digits"]?.toInt() ?: 6,
                    period = params["period"]?.toInt() ?: 30,
                    logoUrl = siteLogo,
                    newOtp = "",
                    remainingTime = 0f,
                    createdAt = System.currentTimeMillis()
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
