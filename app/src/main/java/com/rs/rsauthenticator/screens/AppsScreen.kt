package com.rs.rsauthenticator.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.PrimaryButton
import com.rs.rsauthenticator.components.RsIconButton
import com.rs.rsauthenticator.components.ScreenHeader
import com.rs.rsauthenticator.components.Toast
import com.rs.rsauthenticator.components.ToastState

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


data class AppItem(val name: String, val description: String?, val logo: String?)

@Composable
fun AppsScreen(applicationContext: Context, navHostController: NavHostController) {

    val items by remember {
        mutableStateOf(
            listOf(
                AppItem("Home", "\uf015", "home"),                // Home Icon
                AppItem("Registration", "\uf234", "registration"), // User Plus Icon
                AppItem("Login", "\uf2f6", "login"),              // Sign In Icon
                AppItem("Forgot Password", "\uf084", "forgot_password"), // Key Icon
                AppItem("About", "\uf05a", "about"),             // Info Circle Icon
                AppItem("Apps", "\uf3e0", "apps"),               // Grid Layout / Apps Icon
                AppItem("Home", "\uf015", "home"),                // Home Icon
                AppItem("Registration", "\uf234", "registration"), // User Plus Icon
                AppItem("Login", "\uf2f6", "login"),              // Sign In Icon
                AppItem("Forgot Password", "\uf084", "forgot_password"), // Key Icon
                AppItem("About", "\uf05a", "about"),             // Info Circle Icon
                AppItem("Apps", "\uf3e0", "apps"),               // Grid Layout / Apps Icon
                AppItem("Home", "\uf015", "home"),                // Home Icon
                AppItem("Registration", "\uf234", "registration"), // User Plus Icon
                AppItem("Login", "\uf2f6", "login"),              // Sign In Icon
                AppItem("Forgot Password", "\uf084", "forgot_password"), // Key Icon
                AppItem("About", "\uf05a", "about"),             // Info Circle Icon
                AppItem("Apps", "\uf3e0", "apps"),               // Grid Layout / Apps Icon
                AppItem("Home", "\uf015", "home"),                // Home Icon
                AppItem("Registration", "\uf234", "registration"), // User Plus Icon
                AppItem("Login", "\uf2f6", "login"),              // Sign In Icon
                AppItem("Forgot Password", "\uf084", "forgot_password"), // Key Icon
                AppItem("About", "\uf05a", "about"),             // Info Circle Icon
                AppItem("Apps", "\uf3e0", "apps"),               // Grid Layout / Apps Icon
            )
        )
    }


    var toastState by remember {
        mutableStateOf(
            ToastState(
                isOpen = false,
                isSuccess = false,
                message = ""
            )
        )
    }
    val coroutineScope = rememberCoroutineScope()

    suspend fun handleLogin() {
        try {


        } catch (ex: Exception) {
            println(ex)

            toastState = toastState.copy(
                isOpen = true,
                isSuccess = false,
                message = ex.message
            )

        } finally {
            coroutineScope.launch {
                delay(5000)
                toastState = toastState.copy(isOpen = false)
            }
        }
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

        ScreenHeader(
            navigate = {
                navHostController.navigate("home")
            },
            title = "Connected Apps",
            px = 0.dp
        )

        Toast(modifier = Modifier, toastState = toastState)


        Column(
            modifier = Modifier
                .padding(16.dp, 0.dp)
                .padding(0.dp, 40.dp, 0.dp, 0.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            LazyColumn(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp)) {
                items(items) { item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // Handle navigation or action on item click
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .background(Color(0x19FFFFFF), shape = RoundedCornerShape(10.dp))
                                .padding(16.dp, 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {

                            Image(
                                painter = rememberAsyncImagePainter("https://avatars.githubusercontent.com/u/99707905?v=4"),
                                contentDescription = "Rs Authenticator Logo",
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape),
//                                    .align(Alignment.CenterHorizontally),
                                contentScale = ContentScale.Crop,
                            )

                            Column {
                                CustomText(
                                    text = item.name,
                                    fs = 24.sp
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                CustomText(
                                    text = item.name,
                                )

                            }


                        }
                    }

                }


            }
        }

        RsIconButton(modifier = Modifier
            .zIndex(300F)
            .align(Alignment.BottomEnd)
            .padding(20.dp),
            w = 50.dp,
            h = 50.dp,
            bgColor = Color(0x54FFFFFF),
            onClick = {
                navHostController.navigate("connect_app")
            }) {
            CustomText(icon = "\uf055", fs = 26.sp)
        }
    }
}
