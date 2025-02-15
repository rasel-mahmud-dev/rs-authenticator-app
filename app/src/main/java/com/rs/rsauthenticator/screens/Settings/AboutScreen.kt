package com.rs.rsauthenticator

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.ScreenHeader

@Composable
fun AboutScreen(applicationContext: Context, navHostController: NavHostController) {


    RsColumn() {

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
                text = "About",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fs = 24.sp,
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "This application is designed to enhance user productivity by providing a clean and intuitive interface.\n\n" +
                        "Explore its features and enjoy a seamless experience tailored to your needs.\n\n" +
                        "Explore its features and enjoy a seamless experience tailored to your needs.\n\n" +
                        "Explore its features and enjoy a seamless experience tailored to your needs.\n\n" +
                        "Explore its features and enjoy a seamless experience tailored to your needs.\n\n" +
                        "Explore its features and enjoy a seamless experience tailored to your needs.\n\n" +
                        "Explore its features and enjoy a seamless experience tailored to your needs." +
                        "Explore its features and enjoy a seamless experience tailored to your needs." +
                        "Explore its features and enjoy a seamless experience tailored to your needs.\n\n" +
                        "We aim to deliver fast, reliable, and secure solutions to meet your goals.",
                color = Color.Gray,
                fontSize = 16.sp,
                style = MaterialTheme.typography.bodyLarge
            )


            // Features Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0x16FFFFFF))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Key Features:",
                        color = Color.White,
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "• Fast and reliable performance",
                        color = Color(0xFFE0E0E0),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "• Intuitive user interface",
                        color = Color(0xFFE0E0E0),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "• Secure and private data handling",
                        color = Color(0xFFE0E0E0),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }


        }

    }
}
