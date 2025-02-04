package com.rs.rsauthenticator.state


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.gson.Gson


data class Device(
    val _id: String,
    val userId: String,
    val model: String,
    val createdAt: String
)

data class Auth(
    val email: String,
    val _id: String,
    val deviceId: String,
    val username: String,
    var token: String?,
    var devices: List<Device>?
)


object AuthState {

    private const val PREFS_NAME = "auth_prefs"
    private const val KEY_AUTH = "auth"
    private const val KEY_TOKEN = "auth_token"

    var auth by mutableStateOf<Auth?>(null)

    // Load auth info when the app starts
    fun loadAuthFromPreferences() {
        val gson = Gson()
//        val authJson = sharedPreferences.getString(KEY_AUTH, null)

//        return if (authJson != null) {
//            gson.fromJson(authJson, Auth::class.java)?.apply {
//                this.token = sharedPreferences.getString(KEY_TOKEN, null)
//            }
//        } else {
//            null
//        }
    }

    fun initializeAuthState() {
//        auth = loadAuthFromPreferences()
    }

    fun setAuthInfo(auth: Auth?) {
        AuthState.auth = auth
        saveToPreferences()
    }

    fun clearAuthInfo() {
        auth = null
        clearPreferences()
    }

    private fun saveToPreferences() {
//        val editor = sharedPreferences.edit()
//        val gson = Gson()
//        if (auth != null) {
//            val authJson = gson.toJson(auth)
//            editor.putString(KEY_AUTH, authJson)
//            editor.putString(KEY_TOKEN, auth!!.token)
//        } else {
//            editor.remove(KEY_AUTH)
//            editor.remove(KEY_TOKEN)
//        }
//        editor.apply()
    }

    private fun clearPreferences() {
//        val editor = sharedPreferences.edit()
//        editor.clear() // Clear all saved data
//        editor.apply()
    }

    fun getToken(): String? {
        // If the auth object is loaded, return the token
        return auth?.token
    }
}
