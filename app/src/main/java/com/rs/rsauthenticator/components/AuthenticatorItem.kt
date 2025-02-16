package com.rs.rsauthenticator.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import com.rs.rsauthenticator.dto.AuthenticatorEntry
import com.rs.rsauthenticator.utils.formatTimestamp


@Composable
fun AuthenticatorItem(entry: AuthenticatorEntry, remainingTime: Int) {
    val progress = (30 - remainingTime % 30) / 30f // Normalize between 0 and 1

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000) // Smooth transition
    )


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
                            painter = rememberAsyncImagePainter(if (entry.logoUrl == "") R.drawable.site else entry.logoUrl),
                            contentDescription = "Rs",
                            modifier = Modifier
                                .size(35.dp)
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
                            text = entry.otpCode.substring(0, 3),
                            fontWeight = FontWeight.Light,
                            fs = 30.sp,
                        )
                        CustomText(
                            color = Color(0xFF1A1A1A),
                            text = entry.otpCode.substring(3),
                            fontWeight = FontWeight.Light,
                            fs = 30.sp,
                        )
                    }

                    RsRow {
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
}