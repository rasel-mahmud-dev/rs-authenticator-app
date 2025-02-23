package com.rs.rsauthenticator

import LockUnlockWrapperScreen
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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

    private val screenOffReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_SCREEN_OFF) {
                Log.d("Screen", "Screen turned off - Locking")
                lockRunnable.run()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        observeLifecycleToInactivity()
        AppState.initialize(this)
        AppStateDbHelper.getInstance(this)

        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
        registerReceiver(screenOffReceiver, filter)

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
        lockHandler.postDelayed(lockRunnable, 10 * 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(screenOffReceiver)
    }
}

