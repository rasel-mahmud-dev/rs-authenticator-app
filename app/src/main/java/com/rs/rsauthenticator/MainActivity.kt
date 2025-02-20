package com.rs.rsauthenticator

import LockUnlockWrapperScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.rs.rsauthenticator.database.AppStateDbHelper
import com.rs.rsauthenticator.state.AuthState
import com.rs.rsauthenticator.ui.theme.RsAuthenticatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        AuthState.initialize(this)
        AppStateDbHelper.getInstance(this)

        setContent {
            RsAuthenticatorTheme {

                val statusBarColor = Color(0xFFF44336)
                val view = LocalView.current
                SideEffect {
                    window.statusBarColor = statusBarColor.toArgb()
                    val insetsController = WindowCompat.getInsetsController(window, view)
                    insetsController.isAppearanceLightStatusBars = false
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        LockUnlockWrapperScreen(
                            AppStateDbHelper.getInstance(applicationContext),
                            onUnlock = {}
                        )
                    }
                }
            }
        }
    }
}

