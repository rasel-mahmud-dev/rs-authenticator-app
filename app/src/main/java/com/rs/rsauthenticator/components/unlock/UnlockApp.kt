package com.rs.rsauthenticator.components.unlock

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.PrimaryButton
import com.rs.rsauthenticator.components.form.TextInput


@SuppressLint("RememberReturnType")
@Composable
fun UnlockApp(onUnlock: () -> Unit, storedPin: String) {
    var errorMessage by remember { mutableStateOf("") }
    var inputPin by remember { mutableStateOf(TextFieldValue("")) }
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = shakeAnimation.value.dp)
        ) {
            CustomText(
                icon = "\uf084",
                color = Color.Red,
                fs = 70.sp,
            )

            Text(
                text = "Enter Your PIN",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            TextInput(
                value = inputPin,
                onChange = { inputPin = it },
                label = "",
                keyboardType = KeyboardType.NumberPassword,
                placeholder = "Enter pin",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }

            PrimaryButton(
                onClick = {
                    if (inputPin.text == storedPin) {
                        onUnlock()
                    } else {
                        errorMessage = "Incorrect PIN. Try again!"
                        shakeError = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                label = "Unlock"
            )
        }
    }
}
