package com.rs.rsauthenticator.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex


data class ToastState(var isOpen: Boolean, var isSuccess: Boolean, var message: String)


@Composable
fun Toast(
    modifier: Modifier = Modifier,
    toastState: ToastState
) {

    Log.d("sdfsd", toastState.toString())

    Box(
        modifier = modifier
            .fillMaxSize()
            .zIndex(100F),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = toastState.isOpen,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .zIndex(100F)
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp)
                    .background(
                        if (toastState.isSuccess) {
                            Color(0xFF3682F4)
                        } else {
                            Color(0xFFAB2323)
                        }, shape = RoundedCornerShape(12.dp)
                    )
                    .padding(10.dp, 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    CustomText(
                        modifier = Modifier,
                        icon = "\uf05a",
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Light,
                        fs = 14.sp,
                        color = Color(0x7CE7DADA),
                    )

                    CustomText(
                        modifier = Modifier.weight(1f),
                        text = toastState.message,
                        textAlign = TextAlign.Start,
                        fs = 12.sp,
                        color = Color(0xFFE7DADA)
                    )
                }

                RsIconButton(modifier = Modifier.zIndex(100F), onClick = {
                    toastState.isOpen = false
                }, w = 35.dp, h = 35.dp, bgColor = Color(0xFFF44336)) {
                    CustomText(
                        modifier = Modifier.weight(1f),
                        icon = "\ubaff",
                        textAlign = TextAlign.End,
                        fs = 14.sp,
                        color = Color(0xFFBEAFAF)
                    )
                }
            }
        }
    }
}