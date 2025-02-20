package com.rs.rsauthenticator.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rs.rsauthenticator.components.PrimaryButton
import com.rs.rsauthenticator.components.ScreenHeader
import com.rs.rsauthenticator.components.form.TextInput

@Composable
fun ForgotPasswordScreen(navHostController: NavHostController) {

    val applicationContext = LocalContext.current


    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) {


        ScreenHeader(
            navigate = {
                navHostController.navigate("home")
            },
            title = "Forgot Password.",
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {


            Card(
                modifier = Modifier
                    .fillMaxWidth(),
//                    .padding(horizontal = 16.dp),
//                shape = RoundedCornerShape(16.dp),
//                colors = CardDefaults.cardColors(containerColor = Color(0x16FFFFFF))
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .padding(0.dp, 40.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {


                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Forgot password.",
                            color = Color.Black,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Spacer(Modifier.height(20.dp))


                    TextInput(
                        value = email,
                        placeholder = "Enter email.",
                        onChange = {
                            email = it
                        },
                        label = "Email",
                        keyboardType = KeyboardType.Email
                    )
                    Spacer(Modifier.height(1.dp))


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {

                        Column {

                            Text(
                                text = "Login here.",
                                color = Color.Blue,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .clickable {
                                        navHostController.navigate("login")
                                    }
                            )

                        }
                    }

                    PrimaryButton(
                        modifier = Modifier
                            .padding(0.dp, 20.dp)
                            .align(Alignment.CenterHorizontally),
                        label = "Request",
                        onClick = {},
                        icon = null,
                        px = 80.dp,
                        py = 14.dp,
                    )
                }
            }
        }
    }
}
