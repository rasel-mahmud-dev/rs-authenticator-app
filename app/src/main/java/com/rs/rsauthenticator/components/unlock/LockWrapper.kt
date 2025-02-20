package com.rs.rsauthenticator.components.unlock

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.rs.rsauthenticator.components.UnlockScreen
import com.rs.rsauthenticator.database.AppStateDbHelper
import com.rs.rsauthenticator.layout.MainApp


@Composable
fun LockUnlockWrapperScreen(appStateDbHelper: AppStateDbHelper, onUnlock: () -> Unit) {
    val context = LocalContext.current
    var isUnlocked by remember { mutableStateOf(true) }
    val storedPin by remember { mutableStateOf(appStateDbHelper.getPin()) }
    val isPinEnabled by remember { mutableStateOf(appStateDbHelper.isPinEnabled()) }

    if (isPinEnabled && storedPin.isNullOrEmpty()) {
        isUnlocked = true
    }

    if (isUnlocked) {
        MainApp(context)
    } else {
        UnlockScreen(onUnlock = {
            isUnlocked = true
        }, storedPin = storedPin ?: "")
    }
}
