package com.rs.rsauthenticator.components.Authenticator

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.rs.rsauthenticator.R
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.GradientCircularProgressIndicator
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.dto.AuthenticatorEntry
import com.rs.rsauthenticator.ui.theme.faBrand
import com.rs.rsauthenticator.utils.formatTimestamp
import com.rs.rsauthenticator.utils.iconsMapping


@Composable
fun AuthenticatorItem(
    entry: AuthenticatorEntry,
    remainingTime: Int,
    onClick: () -> Unit
) {
    val progress = (30 - remainingTime % 30) / 30f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000) // Smooth transition
    )

    val icon = iconsMapping.entries.find { entry.issuer.contains(it.key, ignoreCase = true) }?.value

    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxSize()
        .padding(bottom = 16.dp)
        .background(Color(0xFFF3F3F3), shape = RoundedCornerShape(16.dp))
        .clip(shape = RoundedCornerShape(16.dp))

        .clickable {
            onClick()
        }
        .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    if (icon != null) {
                        CustomText(icon = icon, fs = 35.sp, fontFamily = faBrand)
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(if (entry.logoUrl == "") R.drawable.site else entry.logoUrl),
                            contentDescription = "Rs",
                            modifier = Modifier
                                .size(35.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                        )
                    }

                    Column {
                        CustomText(
                            text = entry.issuer,
                            fontWeight = FontWeight.Bold,
                            fs = 16.sp,
                            color = Color.Black,
                            pb = 2.dp
                        )

                        CustomText(
                            text = entry.accountName,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF525252),
                            fs = 13.sp,
                            pb = 4.dp
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

        Column(modifier = Modifier.padding(top = 10.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    val otp = entry.newOtp ?: ""
                    val firstPart = if (otp.length >= 3) otp.substring(0, 3) else otp
                    val secondPart = if (otp.length > 3) otp.substring(3) else ""

                    CustomText(
                        color = Color(0xFF1A1A1A),
                        text = firstPart,
                        fontWeight = FontWeight.Light,
                        fs = 30.sp,
                    )
                    CustomText(
                        color = Color(0xFF1A1A1A),
                        text = secondPart,
                        fontWeight = FontWeight.Light,
                        fs = 30.sp,
                    )
                }

                Row {
                    CustomText(
                        text = "${30 - (remainingTime % 30)} Sec",
                        color = Color(0xFF1A1A1A),
                        fs = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

}