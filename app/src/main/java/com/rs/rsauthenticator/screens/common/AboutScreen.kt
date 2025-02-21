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
fun AboutScreen(navHostController: NavHostController) {

    val applicationContext = LocalContext.current

    Scaffold(
        topBar = {
            ScreenHeader(
                title = "About",
                navigate = { navHostController.popBackStack() }
            )
        },
        content = { padding ->

            RsColumn(modifier = Modifier.padding(padding)){
                RsColumn(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    CustomText(
                        text = "RsAuthenticator",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fs = 24.sp,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Text(
                        text = "RsAuthenticator is a secure authenticator app developed using Kotlin Jetpack Compose. " +
                                "It ensures 100% secure authentication without requiring internet access.\n\n" +
                                "The app follows best practices with reusable Jetpack Compose components, SQLite database " +
                                "integration, multiple layouts, a single activity architecture, and `NavHostController` for seamless navigation.",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    CustomText(
                        text = "Key Features",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fs = 20.sp,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Column(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FeatureItem("Offline Backup & Restore", "Easily back up and restore using JSON format.")
                        FeatureItem("Data Security", "Your authentication data remains safe and private.")
                        FeatureItem("Simplicity", "A seamless user experience with an intuitive interface.")
                        FeatureItem("Elegant Design", "A modern and visually appealing UI.")

                        DeveloperInfo()
                    }



                    Text(text = "Version 1.0.0")

                }
            }
        })
}

@Composable
fun FeatureItem(title: String, description: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "• $title",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(
            text = description,
            color = Color.Gray,
            fontSize = 16.sp
        )
    }
}
