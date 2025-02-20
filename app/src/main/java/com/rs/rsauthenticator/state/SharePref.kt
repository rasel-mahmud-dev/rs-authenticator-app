package com.rs.rsauthenticator.state

import android.content.Context
import android.content.SharedPreferences

class SharePref private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var instance: SharePref? = null

        fun getInstance(context: Context): SharePref {
            return instance ?: synchronized(this) {
                instance ?: SharePref(context.applicationContext).also { instance = it }
            }
        }
    }

    fun setAppInitialized(isInitialized: Boolean) {
        sharedPreferences.edit().putBoolean("app_initialized", isInitialized).apply()
    }

    fun isAppInitialized(): Boolean {
        return sharedPreferences.getBoolean("app_initialized", false)
    }
}
