package com.rs.rsauthenticator.screens.auth

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.PrimaryButton
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.ScreenHeader
import com.rs.rsauthenticator.components.form.TextInput
import com.rs.rsauthenticator.http.services.ApiService
import com.rs.rsauthenticator.state.Auth
import com.rs.rsauthenticator.state.AppState
import com.rs.rsauthenticator.ui.providers.LocalToastController
import com.rs.rsauthenticator.ui.theme.Primary40
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navHostController: NavHostController) {

    val applicationContext = LocalContext.current
    val toastController = LocalToastController.current

    var email by remember { mutableStateOf(TextFieldValue("test@gmail.com")) }
    var password by remember { mutableStateOf(TextFieldValue("123456")) }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    suspend fun handleLogin() {
        try {
            errorMessage = ""
            loading = true
            val res = ApiService.login(email.text, password.text)

            val userId = res?.data?.sessionId
            if (!userId.isNullOrEmpty()) {
                toastController.showToast(
                    message = "Successfully logged user.",
                    isSuccess = true,
                    timeout = 3000
                )

                AppState.setAuthInfo(
                    applicationContext,
                    Auth(
                        email = res.data.email,
                        id = res.data.id,
                        username = res.data.username,
                        token = res.data.token,
                        avatar = res.data.avatar
                    )
                )
                navHostController.navigate("home")
            } else {
                errorMessage = res?.message ?: ""
                toastController.showToast(
                    message = errorMessage,
                    isSuccess = false,
                    timeout = 3000
                )
            }

        } catch (ex: Exception) {
            errorMessage = ex.message ?: "An unexpected error occurred"
            toastController.showToast(
                message = errorMessage,
                isSuccess = false,
                timeout = 3000
            )

        } finally {
            loading = false
        }
    }

    Scaffold(
        topBar = {
            ScreenHeader(
                title = "Login",
                navigate = { navHostController.popBackStack() }
            )
        },
        content = { padding ->


                RsColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    RsColumn(
                        modifier = Modifier
                            .padding(0.dp, 10.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        RsColumn(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            CustomText(
                                text = "Login",
                                color = Color.Black,
                                fs = 28.sp,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        RsColumn(
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            CustomText(
                                text = "Login your account.",
                                color = Color.DarkGray,
                                fs = 16.sp,
                                modifier = Modifier
                            )
                        }

                        errorMessage?.let {
                            CustomText(
                                text = it,
                                color = Color.Red,
                                fs = 14.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

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

                            RsColumn {
                                CustomText(
                                    text = "Don't have an  Account?",
                                    color = Color.DarkGray,
                                    fs = 14.sp,
                                )
                                CustomText(
                                    text = "Create Account",
                                    color = Color.Blue,
                                    fs = 14.sp,
                                    modifier = Modifier
                                        .clickable {
                                            navHostController.navigate("registration")
                                        }
                                )

                            }

                            CustomText(
                                text = "Forgot Password?",
                                color = Color.DarkGray,
                                fs = 14.sp,
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
                            label = if (loading) "Logging in..." else "Submit",
                            onClick = {
                                if (!loading) {
                                    coroutineScope.launch { handleLogin() }
                                }
                            },
                            icon = null,
                            px = 80.dp,
                            py = 14.dp,
                        )
                    }

                    RsColumn(
                        modifier = Modifier
                            .border(1.dp, Primary40, shape = RoundedCornerShape(10.dp))
                            .padding(16.dp)
                    ) {
                        CustomText(
                            fs = 14.sp,
                            color = Color.Gray,
                            text = "Currently, login and registration are optional. It only shows your profile in the app. It will call the server to log in, and registered users will be stored there."
                        )
                    }
                }
            }
        )
}
