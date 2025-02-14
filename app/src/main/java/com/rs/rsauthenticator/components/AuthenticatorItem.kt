package com.rs.rsauthenticator.components

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.rs.rsauthenticator.database.TotpDatabaseHelper
import com.rs.rsauthenticator.dto.AuthenticatorEntry
import com.rs.rsauthenticator.utils.formatTimestamp
import com.rs.rsauthenticator.utils.generateTOTP

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun AuthenticatorItem(entry: AuthenticatorEntry) {

    var remainingTime by remember { mutableLongStateOf(entry.remainingTime) }
    var otpCode by remember { mutableStateOf(entry.otpCode) }
    val scope = rememberCoroutineScope()
    val totpDatabaseHelper = TotpDatabaseHelper.getInstance(LocalContext.current)

    LaunchedEffect(Unit) {
        Log.d("Initial", entry.toString())
        while (true) {
            delay(1000)

            if (remainingTime - System.currentTimeMillis() <= -1) {
                val newOtp = generateTOTP(entry.secret)
                otpCode = newOtp
                remainingTime = System.currentTimeMillis() + 30000

                scope.launch {
                    totpDatabaseHelper.updateTotpEntry(entry.id, newOtp, remainingTime)
                }
            } else {
                remainingTime -= 1
                totpDatabaseHelper.updateOtpRemainingTime(entry.id, remainingTime)
            }
        }
    }

    val animatedProgress = animateFloatAsState(
        targetValue = ((remainingTime - System.currentTimeMillis()) / 30000f).coerceIn(0f, 1f),
        animationSpec = androidx.compose.animation.core.TweenSpec(
            durationMillis = 1000,
            easing = androidx.compose.animation.core.FastOutSlowInEasing
        ), label = ""
    ).value


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 6.dp)
            .background(Color(0xFFF3F3F3), shape = RoundedCornerShape(16.dp))
    ) {

        RsColumn(modifier = Modifier.fillMaxWidth(), px = 16.dp, py = 16.dp) {
            RsColumn(modifier = Modifier.fillMaxWidth()) {
                RsRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    RsRow(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {

                        Image(
                            painter = rememberAsyncImagePainter(entry.logoUrl),
                            contentDescription = "Rs",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                        )

                        RsColumn {
                            CustomText(
                                text = entry.issuer,
                                fontWeight = FontWeight.Bold,
                                fs = 16.sp,
                                color = Color.Black,
                                pb = 2.dp
                            )

                            CustomText(
                                text = formatTimestamp(entry.createdAt),
                                fontWeight = FontWeight.Normal,
                                color = Color(0xFF727272),
                                fs = 13.sp
                            )

                        }
                    }

                    RsColumn() {
                        GradientCircularProgressIndicator(
                            progress = animatedProgress,
                            modifier = Modifier.size(40.dp),
                        )

                    }

                }
            }

            RsColumn(pt = 10.dp) {
                RsRow(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    RsRow(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CustomText(
                            color = Color(0xFF1A1A1A),
                            text = otpCode.substring(0, 3),
                            fontWeight = FontWeight.Light,
                            fs = 30.sp,
                        )
                        CustomText(
                            color = Color(0xFF1A1A1A),
                            text = otpCode.substring(3),
                            fontWeight = FontWeight.Light,
                            fs = 30.sp,
                        )
                    }

                    RsRow {
                        CustomText(
                            text = "${getRemainingSecond(remainingTime)} Sec",
                            color = Color(0xFF1A1A1A),
                            fs = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

fun getRemainingSecond(remainingTime: Long): Long {
    val a = (remainingTime - System.currentTimeMillis()) / 1000
    return   if(a <= -1) 0 else a
}
