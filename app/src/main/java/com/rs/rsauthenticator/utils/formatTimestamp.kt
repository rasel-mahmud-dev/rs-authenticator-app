package com.rs.rsauthenticator.utils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMMM yyyy hh:mm:ss a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
