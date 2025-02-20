package com.rs.rsauthenticator.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.RsRow
import com.rs.rsauthenticator.components.ScreenHeader
import com.rs.rsauthenticator.components.form.TextInput
import com.rs.rsauthenticator.http.services.ApiService
import com.rs.rsauthenticator.ui.providers.LocalToastController
import com.rs.rsauthenticator.ui.theme.AppColors
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(navHostController: NavHostController) {

    var email by remember { mutableStateOf(TextFieldValue("test@gmail.com")) }
    var password by remember { mutableStateOf(TextFieldValue("12345")) }
    var phone by remember { mutableStateOf(TextFieldValue("01799513737")) }
    var firstName by remember { mutableStateOf(TextFieldValue("Test")) }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val toastController = LocalToastController.current
    val coroutineScope = rememberCoroutineScope()

    suspend fun handleRegistration() {
        try {
            loading = false
            errorMessage = ""
            val res = ApiService.register(firstName.text, email.text, password.text)
            println(res)
            val userId = res?.data?.id
            if (!userId.isNullOrEmpty()) {
                toastController.showToast(
                    message = "Successfully registered user.",
                    isSuccess = false,
                    timeout = 3000
                )

                navHostController.navigate("login")
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
                title = "Registration",
                navigate = { navHostController.popBackStack() }
            )
        },
        content = { padding ->
            RsColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                RsColumn(
                    modifier = Modifier
                        .padding(16.dp, 20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {


                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "Create an account.",
                            color = AppColors.Dark40,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    RsColumn(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    ) {
                        CustomText(
                            text = "New account account.",
                            color = AppColors.Dark10,
                            fs = 16.sp,
                            modifier = Modifier
                        )
                    }


                    errorMessage?.let {
                        CustomText(
                            text = it,
                            color = Color.Red,
                            fs = 14.sp,
                            pt = 8.dp
                        )
                    }


                    TextInput(
                        value = firstName,
                        placeholder = "Enter Firstname.",
                        onChange = {
                            firstName = it
                        },
                        label = "First Name",
                        keyboardType = KeyboardType.Text
                    )

                    TextInput(
                        value = email,
                        placeholder = "Enter email.",
                        onChange = {
                            email = it
                        },
                        label = "Email",
                        keyboardType = KeyboardType.Email
                    )

                    TextInput(
                        value = phone,
                        placeholder = "Enter phone.",
                        onChange = {
                            phone = it
                        },
                        label = "Phone",
                        keyboardType = KeyboardType.Phone
                    )

                    TextInput(
                        value = password,
                        placeholder = "Your password",
                        onChange = {
                            password = it
                        },
                        label = "Password",
                        keyboardType = KeyboardType.Password
                    )


                    RsRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        pt = 8.dp,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {

                        Column {
                            CustomText(
                                text = "Already have an Account?",
                                color = Color.DarkGray,
                                fs = 14.sp,
                            )
                            CustomText(
                                text = "Login here",
                                color = Color.Blue,
                                fs = 14.sp,
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
                        label = if (loading) "Creating account..." else "Submit",
                        onClick = {
                            if (!loading) {
                                coroutineScope.launch { handleRegistration() }
                            }
                        },
                        icon = null,
                        px = 80.dp,
                        py = 14.dp,
                    )
                }
            }
        }
    )
}
