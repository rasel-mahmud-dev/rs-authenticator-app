package com.rs.rsauthenticator.dto

import java.net.URLDecoder
import java.nio.charset.StandardCharsets

data class TotpUriData(
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
    val newOtp: String?,
    val remainingTime: Float?
) {
    companion object {
        fun fromUri(uri: String): TotpUriData? {
            return try {
                val decodedUri = URLDecoder.decode(uri, StandardCharsets.UTF_8.name())

                val regex = Regex("otpauth://(\\w+)/(.*)\\?(.*)")
                val matchResult = regex.find(decodedUri) ?: return null

                val type = matchResult.groupValues[1]
                val accountWithIssuer = matchResult.groupValues[2]
                val queryParams = matchResult.groupValues[3]

                val params = queryParams.split("&").associate {
                    val (key, value) = it.split("=")
                    key to value
                }


                val issuerParts = params["issuer"]?.split("|")
                val issuer = issuerParts?.firstOrNull() ?: "Unknown"
                val logoUrl = issuerParts?.getOrNull(1)

                TotpUriData(
                    id = "",
                    protocol = "otpauth",
                    type = type,
                    accountName = accountWithIssuer,
                    secret = params["secret"] ?: "",
                    issuer = issuer,
                    algorithm = params["algorithm"] ?: "SHA1",
                    digits = params["digits"]?.toInt() ?: 6,
                    period = params["period"]?.toInt() ?: 30,
                    logoUrl = logoUrl,
                    newOtp = "",
                    remainingTime = 0f
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
