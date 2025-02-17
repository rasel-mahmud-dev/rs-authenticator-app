package com.rs.rsauthenticator.screens

import android.content.Context
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.PrimaryButton
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.ScreenHeader

@Composable
fun AboutScreen(applicationContext: Context, navHostController: NavHostController) {


    Scaffold(
        topBar = {
            ScreenHeader(
                title = "About",
                navigate = { navHostController.popBackStack() }
            )
        },
        content = { padding ->

            RsColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                CustomText(
                    text = "About",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fs = 24.sp,
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = "This application is designed to enhance user productivity by providing a clean and intuitive interface.\n\n" +
                            "Explore its features and enjoy a seamless experience tailored to your needs.\n\n",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    style = MaterialTheme.typography.bodyLarge
                )


                PrimaryButton(modifier = Modifier
                    .clip(shape = CircleShape),
                    label = "Project Details",
                    onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://github.com/rasel-mahmud-dev/rs-authenticator-app")
                        )
                        applicationContext.startActivity(intent)
                    })
            }
        })
}
