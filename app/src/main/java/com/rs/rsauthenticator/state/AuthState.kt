package com.rs.rsauthenticator.state

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.rs.rsauthenticator.database.AppStateDbHelper


data class Auth(
    val email: String,
    val _id: String,
    val username: String,
    val avatar: String?,
    var token: String?,
)

object AuthState {

    var auth by mutableStateOf<Auth?>(null)
        private set

    fun initialize(context: Context) {
        auth = AppStateDbHelper.getInstance(context).getAuth()
    }

    fun setAuthInfo(context: Context, newAuth: Auth?) {
        auth = newAuth
        AppStateDbHelper.getInstance(context).saveAuth(newAuth!!)
    }

    fun clearAuthInfo(context: Context) {
        auth = null
        AppStateDbHelper.getInstance(context).clearAuth()
    }

    fun getToken(): String? {
        return auth?.token
    }
}
