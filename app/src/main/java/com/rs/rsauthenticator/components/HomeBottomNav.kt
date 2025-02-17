package com.rs.rsauthenticator.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun HomeBottomNav(activeTab: String, onChangeTab: (t: String) -> Unit) {
    RsColumn(
        modifier = Modifier
            .fillMaxWidth(),
        bgColor = Color(0xFFE7E7E7),
        verticalArrangement = Arrangement.Center
    ) {

        RsRow(
            modifier = Modifier.fillMaxWidth(),
            py = 8.dp,
            horizontalArrangement = Arrangement.Absolute.SpaceAround
        ) {

            RsColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                RsIconButton(
                    onClick = { onChangeTab("home") },
                    modifier = Modifier
                        .padding(0.dp)
                        .width(70.dp)
                        .height(35.dp),
                    radius = 60.dp,
                    bgColor = if (activeTab === "home") Color(0x32F44336) else Color.Transparent
                ) {
                    CustomText(
                        icon = "\uf015",
                        color = if (activeTab === "home") Color(0xFFF44336) else Color(
                            0xFF6B6B6B
                        ),
                        fontWeight = FontWeight.Bold,
                        fs = 20.sp,
                    )
                }
                CustomText(
                    text = "Tokens",
                    fs = 13.sp,
                    color = if (activeTab === "home") Color(0xFFF44336) else Color(
                        0xFF6B6B6B
                    ),
                    fontWeight = FontWeight.Medium,
                    pt = 1.dp
                )

            }

            RsColumn(horizontalAlignment = Alignment.CenterHorizontally) {

                RsIconButton(
                    onClick = { onChangeTab("settings") },
                    modifier = Modifier
                        .padding(0.dp)
                        .width(70.dp)
                        .height(35.dp),
                    radius = 60.dp,
                    bgColor = if (activeTab === "settings") Color(0x32F44336) else Color.Transparent
                ) {
                    CustomText(
                        icon = "\uf013",
                        color = if (activeTab === "settings") Color(0xFFF44336) else Color(
                            0xFF6B6B6B
                        ),
                        fontWeight = FontWeight.Bold,
                        fs = 20.sp
                    )
                }

                CustomText(
                    text = "Settings",
                    fs = 13.sp,
                    color = if (activeTab === "settings") Color(0xFFF44336) else Color(
                        0xFF6B6B6B
                    ),
                    fontWeight = FontWeight.Medium,
                    pt = 1.dp
                )

            }


        }

    }
}
