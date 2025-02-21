package com.rs.rsauthenticator.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.rs.rsauthenticator.R
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.PrimaryButton
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.ScreenHeader
import com.rs.rsauthenticator.state.AppState


@Composable
fun ProfileScreen(navHostController: NavHostController) {

    val applicationContext = LocalContext.current


    val auth = AppState.auth

    Scaffold(
        topBar = {
            ScreenHeader(
                title = "Profile",
                navigate = { navHostController.popBackStack() }
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {

                RsColumn(
                    modifier = Modifier.fillMaxWidth(),
                    pt = 10.dp
                ) {

                    RsColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        pt = 40.dp
                    ) {

                        Image(
                            painter = rememberAsyncImagePainter(if (auth?.avatar.isNullOrEmpty()) R.drawable.avatar else auth?.avatar),
                            contentDescription = "Rs Authenticator Logo",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                        )

                        CustomText(
                            modifier = Modifier,
                            text = auth?.username ?: "Guest.",
                            fs = 16.sp,
                            pt = 20.dp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        CustomText(
                            modifier = Modifier,
                            text = auth?.email ?: "guest@example.com",
                            fs = 14.sp,
                            pt = 5.dp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray
                        )


                        RsColumn(pt = 20.dp) {
                            if (auth != null) {
                                PrimaryButton(onClick = {
                                    AppState.clearAuthInfo(applicationContext)
                                }, label = "Logout")
                            } else {
                                PrimaryButton(onClick = {
                                    navHostController.navigate("login")
                                }, label = "Login")
                            }
                        }
                    }
                }
            }
        }
    )
}

