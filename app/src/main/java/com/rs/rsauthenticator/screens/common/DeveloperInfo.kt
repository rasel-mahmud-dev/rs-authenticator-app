package com.rs.rsauthenticator.screens.common

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.rs.rsauthenticator.R
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.PrimaryButton
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.ScreenHeader
import com.rs.rsauthenticator.utils.openLinkedInProfile


@Composable
fun DeveloperInfo() {
    val applicationContext = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = rememberAsyncImagePainter(R.drawable.avatar),
            contentDescription = "Rs Authenticator Logo",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )

        CustomText(
            pt = 10.dp,
            text = "Rasel Mahmud",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fs = 20.sp
        )

        CustomText(
            pt = 6.dp,
            text = "Web Developer | Javascript Enthusiast",
            color = Color.Gray,
            fs = 16.sp
        )

        Column(
            modifier = Modifier.padding(top = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            CustomText(
                text = "Skills:",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fs = 18.sp
            )
            CustomText(
                text = "• Javascript/Golang/Kotlin,",
                color = Color.Gray,
                fs = 16.sp
            )
            CustomText(
                text = "• Android Development",
                color = Color.Gray,
                fs = 16.sp
            )
            CustomText(
                text = "• Jetpack Compose",
                color = Color.Gray,
                fs = 16.sp
            )
            CustomText(
                text = "• RESTful APIs",
                color = Color.Gray,
                fs = 16.sp
            )
            CustomText(
                text = "• SQLite, Room Database",
                color = Color.Gray,
                fs = 16.sp
            )
            CustomText(
                text = "• Git, GitHub",
                color = Color.Gray,
                fs = 16.sp
            )

            PrimaryButton(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .clip(shape = CircleShape),
                label = "Connect on LinkedIn",
                onClick = {
                    openLinkedInProfile(applicationContext)
                }
            )
        }
    }
}