package com.rs.rsauthenticator.screens

import android.content.Context
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
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.PrimaryButton
import com.rs.rsauthenticator.components.ScreenHeader
import com.rs.rsauthenticator.components.Toast
import com.rs.rsauthenticator.components.ToastState
import com.rs.rsauthenticator.components.form.TextInput
import com.rs.rsauthenticator.http.services.ApiService
import com.rs.rsauthenticator.state.Auth
import com.rs.rsauthenticator.state.AuthState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(applicationContext: Context, navHostController: NavHostController) {

    var email by remember { mutableStateOf(TextFieldValue("test@gmail.com")) }
    var password by remember { mutableStateOf(TextFieldValue("12345")) }


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
            val res = ApiService.login(email.text, password.text)
            val userId = res?.data?.user?._id
            if (!userId.isNullOrEmpty()) {
                toastState = toastState.copy(
                    isOpen = true,
                    isSuccess = true,
                    message = "Successfully logged user."
                )


                AuthState.setAuthInfo(
                    applicationContext,
                    Auth(
                        email = res.data.user.email,
                        _id = res.data.user._id,
                        username = res.data.user.username,
                        token = res.data.session.token,
                        avatar = res.data.user.avatar
                    )
                )
                navHostController.navigate("home")
            }

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

            .padding(16.dp)

    ) {


        ScreenHeader(
            navigate = {
                navHostController.navigate("home")
            },
            title = "Login"
        )

        Toast(modifier = Modifier, toastState = toastState)

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
                        CustomText(
                            text = "Login",
                            color = Color.Black,
                            fs = 28.sp,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Login your account.",
                            color = Color.DarkGray,
                            fontSize = 16.sp,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
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

                    TextInput(
                        value = password,
                        placeholder = "Your password",
                        onChange = {
                            password = it
                        },
                        label = "Password",
                        keyboardType = KeyboardType.Password
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {

                        Column {
                            Text(
                                text = "Don't have an  Account?",
                                color = Color.DarkGray,
                                fontSize = 14.sp,
                            )
                            Text(
                                text = "Create Account",
                                color = Color.Blue,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .clickable {
                                        navHostController.navigate("registration")
                                    }
                            )

                        }

                        Text(
                            text = "Forgot Password?",
                            color = Color.DarkGray,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .clickable {
                                    navHostController.navigate("forgot_password")
                                }
                        )
                    }

                    PrimaryButton(
                        modifier = Modifier
                            .padding(0.dp, 20.dp)
                            .align(Alignment.CenterHorizontally),
                        label = "Submit",
                        onClick = {
                            coroutineScope.launch {
                                handleLogin()
                            }

                        },
                        icon = null,
                        px = 80.dp,
                        py = 14.dp,
                    )
                }
            }
        }
    }
}
