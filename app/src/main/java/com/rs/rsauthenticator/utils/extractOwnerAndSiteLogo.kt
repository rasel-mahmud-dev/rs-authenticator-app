package com.rs.rsauthenticator.utils

fun extractOwnerAndSiteLogo(otpUri: String): Pair<String, String> {
    val parts = otpUri.split("https://", "http://")

    return if (parts.size == 2) {
        val emailPart = parts[0].substringAfter("totp/").substringBefore("%7C")
        val avatarUrl = "https://${parts[1].substringBefore("?")}"
        Pair(emailPart, avatarUrl)
    } else {
        val accountName = otpUri.substringAfter("otpauth://totp/").substringBefore("?")
        if (accountName != "") return Pair(accountName, "")
        Pair(otpUri, "")
    }
}
