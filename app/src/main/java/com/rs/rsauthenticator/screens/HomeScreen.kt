package com.rs.rsauthenticator.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.rs.rsauthenticator.components.CustomText

data class MenuItem(val name: String, val iconName: String, val route: String)

var items = listOf(
    MenuItem("Home", "\uf015", "home"),                // Home Icon
    MenuItem("Registration", "\uf234", "registration"), // User Plus Icon
    MenuItem("Login", "\uf2f6", "login"),              // Sign In Icon
    MenuItem("Forgot Password", "\uf084", "forgot_password"), // Key Icon
    MenuItem("About", "\uf05a", "about"),             // Info Circle Icon
    MenuItem("Apps", "\uf3e0", "apps")               // Grid Layout / Apps Icon
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(applicationContext: Context, navController: NavHostController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFEF4720), Color(0xFF89A5CE))
                )
            )
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Column(
                modifier = Modifier
                    .padding(0.dp, 20.dp, 0.dp, 40.dp)
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
                CustomText(
                    modifier = Modifier,
                    text = "Rs Authenticator",
                    fs = 14.sp,
                    pt = 5.dp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }


            FlowRow(
                modifier = Modifier.padding(0.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {

                items.forEach {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color(0x36D3E9FF), Color(0x4BFFFFFF))
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                navController.navigate(it.route)
                            }


                    ) {
                        Column(
                            modifier = Modifier
                                .width(160.dp)
                                .height(160.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            CustomText(icon = it.iconName, fs = 25.sp, color = Color(0xFFF44336))
                            Spacer(Modifier.height(8.dp))
                            CustomText(
                                text = it.name, textAlign = TextAlign.Center,
                                color = Color(
                                    0xFFEFEFEF
                                ),
                                fs = 12.sp
                            )
                        }
                    }
                }
            }
        }
        CustomText(
            modifier = Modifier.align(Alignment.BottomCenter),
            pb = 40.dp,
            text = "Rs Authenticator: v:0.0.1",
            fs = 10.sp,
            color = Color.White,
        )
    }
}
