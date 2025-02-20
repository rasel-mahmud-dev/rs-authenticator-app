package com.rs.rsauthenticator.components.unlock

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.RsIconButton
import com.rs.rsauthenticator.components.unlock.PinKeyboard
import com.rs.rsauthenticator.database.AppStateDbHelper
import com.rs.rsauthenticator.ui.theme.AppColors
import com.rs.rsauthenticator.ui.theme.faSolid


@SuppressLint("RememberReturnType")
@Composable
fun UnlockScreen(onUnlock: () -> Unit, storedPin: String) {
    var shakeError by remember { mutableStateOf(false) }

    val shakeAnimation = remember { Animatable(0f) }

    LaunchedEffect(shakeError) {
        if (shakeError) {
            shakeAnimation.animateTo(
                targetValue = 10f,
                animationSpec = keyframes {
                    durationMillis = 300
                    0f at 0
                    -10f at 50
                    10f at 100
                    -10f at 150
                    10f at 200
                    0f at 300
                }
            )
            shakeError = false
        }
    }

    val context = LocalContext.current
    val db = AppStateDbHelper.getInstance(context)

    fun handleSetupPin(pin: String) {
        if (pin != storedPin) {
            shakeError = true
        } else {
            onUnlock()
        }
    }

    Scaffold(topBar = {

    }, content = { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
                .fillMaxSize()
                .offset(x = shakeAnimation.value.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {

            RsIconButton(w = 80.dp, h = 80.dp, bgColor = AppColors.Primary40, radius = 20.dp) {
                CustomText(
                    icon = "\ue423", fontFamily = faSolid, color = Color.White, fs = 35.sp
                )
            }
            Spacer(modifier = Modifier.height(36.dp))
            CustomText(
                text = "Insert your PIN.",
                color = AppColors.Dark40,
                fs = 20.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(16.dp))

            PinKeyboard {
                handleSetupPin(it)
            }
        }
    })
}

