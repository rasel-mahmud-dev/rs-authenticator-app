package com.rs.rsauthenticator.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.HomeBottomNav
import com.rs.rsauthenticator.components.PrimaryButton
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.SettingScreen


@Composable
fun HomeScreen2(applicationContext: Context, navController: NavHostController) {

    var activeTab by remember { mutableStateOf("tokens") }

    LaunchedEffect(Unit) {
        activeTab = "tokens"
    }


    Box(

        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFEF4720), Color(0xFF89A5CE))
                )
            )
    ) {


        RsColumn {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1A1A1A))
            ) {

                if (activeTab == "tokens") {
                    RsColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 16.dp),
                    ) {

                        Column(
                            modifier = Modifier
                                .padding(0.dp, 20.dp, 0.dp, 40.dp)
                        ) {

                            RsColumn(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter("https://avatars.githubusercontent.com/u/99707905?v=4"),
                                    contentDescription = "Rs Authenticator Logo",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .align(Alignment.CenterHorizontally),
                                    contentScale = ContentScale.Crop,
                                )
                            }

                            RsColumn(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CustomText(
                                    modifier = Modifier,
                                    text = "Rs Authenticator",
                                    fs = 14.sp,
                                    pt = 5.dp,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            RsColumn(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CustomText(
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier,
                                    text = "Rs Authenticator Authenticator  Authenticator  Authenticator sdfsd Authenticator  sd Authenticator",
                                    fs = 14.sp,
                                    pt = 5.dp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.White
                                )
                            }
                        }

                        PrimaryButton(
                            onClick = {},
                            modifier = Modifier.padding(3.dp),
                            label = "sdf",
                            icon = ""
                        )


                    }

                } else {

                    SettingScreen(applicationContext, navController)
                }


                RsColumn(modifier = Modifier.align(Alignment.BottomCenter)) {
                    HomeBottomNav(activeTab = activeTab, onChangeTab = { activeTab = it })
                }


            }
        }
    }
}
