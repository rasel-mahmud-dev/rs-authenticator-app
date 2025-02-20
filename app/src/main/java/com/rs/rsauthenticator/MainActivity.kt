package com.rs.rsauthenticator

import LockUnlockWrapperScreen
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.rs.rsauthenticator.database.AppStateDbHelper
import com.rs.rsauthenticator.state.AppState
import com.rs.rsauthenticator.ui.providers.ToastProvider
import com.rs.rsauthenticator.ui.theme.AppColors
import com.rs.rsauthenticator.ui.theme.RsAuthenticatorTheme

class MainActivity : ComponentActivity() {

    private val lockHandler = Handler(Looper.getMainLooper())
    private val lockRunnable = Runnable {
        Log.d("Screen", "locked")
        if (AppStateDbHelper.getInstance(this).isPinEnabled()) {
            AppState.updateLockState(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        observeLifecycleToInactivity()
        AppState.initialize(this)
        AppStateDbHelper.getInstance(this)

        setContent {
            RsAuthenticatorTheme {
                val view = LocalView.current
                SideEffect {
                    window.statusBarColor = AppColors.Primary40.toArgb()
                    val insetsController = WindowCompat.getInsetsController(window, view)
                    insetsController.isAppearanceLightStatusBars = false
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {

                        ToastProvider {
                            LockUnlockWrapperScreen(
                                AppStateDbHelper.getInstance(applicationContext),
                            )
                        }
                    }

                }
            }
        }
    }

    private fun observeLifecycleToInactivity() {
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_RESUME -> resetInactivityTimer()
                    Lifecycle.Event.ON_PAUSE -> startInactivityTimer()
                    else -> {}
                }
            }
        })
    }

    private fun resetInactivityTimer() {
        lockHandler.removeCallbacks(lockRunnable)
    }

    private fun startInactivityTimer() {
        lockHandler.postDelayed(lockRunnable, 30 * 1000)
    }
}

