package com.rs.rsauthenticator.screens.Settings


import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.rs.rsauthenticator.components.RsRow
import com.rs.rsauthenticator.components.ScreenHeader
import com.rs.rsauthenticator.state.AuthState


@Composable
fun ProfileScreen(applicationContext: Context, navHostController: NavHostController) {

    val auth = AuthState.auth

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
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                RsColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 80.dp)
                ) {

                    RsRow(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {

                        Image(
                            painter = rememberAsyncImagePainter( auth?.avatar ?: R.drawable.avatar),
                            contentDescription = "Rs Authenticator Logo",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                        )



                        RsColumn {
                            CustomText(
                                modifier = Modifier,
                                text = auth?.username ?: "Guest.",
                                fs = 16.sp,
                                pt = 5.dp,
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
                        }

                    }

                    if (auth != null) {
                        PrimaryButton(onClick = {
                            AuthState.clearAuthInfo(applicationContext)
                        }, label = "Logout")
                    } else {
                        PrimaryButton(onClick = {
                            navHostController.navigate("login")
                        }, label = "Login")
                    }
                }
            }
        }
    )
}

