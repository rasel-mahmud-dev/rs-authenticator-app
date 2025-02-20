package com.rs.rsauthenticator.state

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.rs.rsauthenticator.database.AppStateDbHelper


data class Auth(
    val email: String,
    val id: String,
    val username: String,
    val avatar: String?,
    var token: String?,
)

object AppState {

    var auth by mutableStateOf<Auth?>(null)
        private set

    var isLocked by mutableStateOf(true)
    var isAppLockEnabled by mutableStateOf(true)

    fun initialize(context: Context) {
        val appStateDbHelper = AppStateDbHelper.getInstance(context)
        auth = appStateDbHelper.getAuth()
        isLocked = appStateDbHelper.isPinEnabled()
        isAppLockEnabled = isLocked
    }

    fun setAuthInfo(context: Context, newAuth: Auth?) {
        auth = newAuth
        AppStateDbHelper.getInstance(context).saveAuth(newAuth!!)
    }

    fun clearAuthInfo(context: Context) {
        auth = null
        AppStateDbHelper.getInstance(context).clearAuth()
    }

    fun updateLockState(_isLocked: Boolean) {
        isLocked = _isLocked
    }

    fun changeAppLockEnabled(_isAppLockEnabled: Boolean) {
        isAppLockEnabled = _isAppLockEnabled
    }

    fun getToken(): String? {
        return auth?.token
    }
}
