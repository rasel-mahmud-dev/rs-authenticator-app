package com.rs.rsauthenticator.screens.common

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.PrimaryButton
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.ScreenHeader


@Composable
fun FeaturesScreen(navHostController: NavHostController) {

    val applicationContext = LocalContext.current


    RsColumn {
        ScreenHeader(title = "Back", navigate = {
            navHostController.popBackStack()
        })

        RsColumn(
            pt = 40.dp,
            px = 16.dp,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomText(
                text = "Key Features.",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fs = 24.sp,
                style = MaterialTheme.typography.headlineMedium
            )

            RsColumn(
                pt = 20.dp,
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FeatureItem("Authentication (Login, Registration, Forgot Password).")
                FeatureItem("App Security (Unlock/Lock with PIN, Biometrics).")
                FeatureItem("Authenticator Code Generation (TOTP-based 2FA).")
                FeatureItem("Database Preservation (Stores data securely with SQLite).")
                FeatureItem("Camera QR Code Scanning (Scan QR codes to add 2FA accounts).")
            }

            PrimaryButton(
                modifier = Modifier.clip(shape = CircleShape),
                label = "Project Details",
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/rasel-mahmud-dev/rs-authenticator-app")
                    )
                    applicationContext.startActivity(intent)
                })
        }
    }
}

@Composable
fun FeatureItem(text: String) {
    CustomText(
        pb = 10.dp,
        text = text,
        color = Color.DarkGray,
        fs = 16.sp,
        style = MaterialTheme.typography.bodyLarge
    )
}
