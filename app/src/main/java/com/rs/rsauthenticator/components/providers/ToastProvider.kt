package com.rs.rsauthenticator.ui.providers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.rs.rsauthenticator.components.Toast
import kotlinx.coroutines.delay

data class ToastState(
    var isOpen: Boolean = false,
    val isSuccess: Boolean = false,
    val message: String = "",
    val timeout: Long? = 1000
)


class ToastController {
    private val toastQueue = mutableListOf<ToastState>()
    private var currentToastIndex = 0

    var toastState by mutableStateOf(ToastState())
        private set

    fun showToast(message: String, isSuccess: Boolean = true, timeout: Long? = null) {
        toastQueue.add(
            ToastState(
                isOpen = true,
                isSuccess = isSuccess,
                message = message,
                timeout = timeout
            )
        )

        if (!toastState.isOpen && toastQueue.isNotEmpty()) {
            toastState = toastQueue[currentToastIndex]
        }
    }

    fun hideToast() {
        if (toastQueue.isNotEmpty()) {
            toastQueue.removeAt(0)
            if (toastQueue.isNotEmpty()) {
                toastState = toastQueue[toastQueue.size - 1]
            } else {
                toastState = toastState.copy(isOpen = false)
            }
        } else {
            toastState = toastState.copy(isOpen = false)
        }
    }
}


val LocalToastController = compositionLocalOf { ToastController() }

@Composable
fun ToastProvider(content: @Composable () -> Unit) {
    val toastController = remember { ToastController() }

    if (toastController.toastState.isOpen) {
        LaunchedEffect(toastController.toastState) {
            delay(toastController.toastState.timeout ?: 500)
            toastController.hideToast()
        }
    }

    CompositionLocalProvider(LocalToastController provides toastController) {
        Box(modifier = Modifier.fillMaxSize()) {
            Toast(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .zIndex(1000000F),
                toastState = toastController.toastState
            )
            content()
        }
    }
}
