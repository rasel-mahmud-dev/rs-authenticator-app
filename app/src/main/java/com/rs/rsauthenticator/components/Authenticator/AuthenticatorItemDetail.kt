package com.rs.rsauthenticator.components.Authenticator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.PrimaryButton
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.database.AppStateDbHelper
import com.rs.rsauthenticator.dto.AuthenticatorEntry
import com.rs.rsauthenticator.ui.providers.LocalToastController
import com.rs.rsauthenticator.ui.theme.AppColors
import com.rs.rsauthenticator.utils.formatTimestamp


@Composable
fun AuthenticatorItemDetail(entry: AuthenticatorEntry, onDelete: (secret: String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .padding(16.dp)
    ) {

        RsColumn(modifier = Modifier.fillMaxWidth()) {
            RsColumn {
                CustomText(
                    text = entry.issuer,
                    fontWeight = FontWeight.Bold,
                    fs = 22.sp,
                    color = Color.Black,
                )

                RsColumn(pt = 16.dp) {
                    CustomText(
                        text = "Account Name",
                        fontWeight = FontWeight.Medium,
                        fs = 16.sp,
                        color = AppColors.Dark10,
                    )
                    CustomText(
                        pt = 6.dp,
                        text = entry.accountName,
                        fontWeight = FontWeight.Medium,
                        color = AppColors.Dark40,
                        fs = 18.sp,
                    )
                }
            }


            RsColumn(pt = 16.dp) {
                CustomText(
                    color = AppColors.Dark10,
                    text = "Algorithm",
                    fontWeight = FontWeight.Medium,
                    fs = 16.sp,
                )
                CustomText(
                    pt = 6.dp,
                    color = AppColors.Dark40,
                    text = entry.algorithm,
                    fontWeight = FontWeight.SemiBold,
                    fs = 16.sp,
                )
            }

            RsColumn(pt = 16.dp) {
                CustomText(
                    color = AppColors.Dark10,
                    text = "Secret",
                    fontWeight = FontWeight.Medium,
                    fs = 16.sp,
                )
                CustomText(
                    pt = 6.dp,
                    color = AppColors.Dark40,
                    text = entry.secret,
                    fontWeight = FontWeight.SemiBold,
                    fs = 16.sp,
                )
            }

            RsColumn(pt = 16.dp) {
                CustomText(
                    color = AppColors.Dark10,
                    text = "Added on",
                    fontWeight = FontWeight.Medium,
                    fs = 16.sp,
                )
                CustomText(
                    text = formatTimestamp(entry.createdAt),
                    fontWeight = FontWeight.Normal,
                    color = AppColors.Dark40,
                    pt = 6.dp,
                    fs = 13.sp
                )
            }
            RsColumn(
                modifier = Modifier.fillMaxWidth(),
                pt = 16.dp,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PrimaryButton(
                    label = "Delete",
                    onClick = { onDelete(entry.secret) },
                    px = 50.dp
                )
            }
        }
    }
}