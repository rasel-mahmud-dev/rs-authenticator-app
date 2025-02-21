package com.rs.rsauthenticator.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun openLinkedInProfile(applicationContext: Context) {
    val linkedInUri = Uri.parse("linkedin://profile/rasel-mahmud-dev")
    val webUri = Uri.parse("https://www.linkedin.com/in/rasel-mahmud-dev/")

    val intent = Intent(Intent.ACTION_VIEW, linkedInUri).apply {
        `package` = "com.linkedin.android"
    }

    try {
        applicationContext.startActivity(intent)
    } catch (e: Exception) {
        applicationContext.startActivity(Intent(Intent.ACTION_VIEW, webUri))
    }
}