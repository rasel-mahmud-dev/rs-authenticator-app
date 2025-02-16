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
import com.rs.rsauthenticator.components.PrimaryButton
import com.rs.rsauthenticator.components.ScreenHeader
import com.rs.rsauthenticator.components.form.TextInput
import com.rs.rsauthenticator.http.services.ApiService
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(applicationContext: Context, navHostController: NavHostController) {

    var email by remember { mutableStateOf(TextFieldValue("test@gmail.com")) }
    var password by remember { mutableStateOf(TextFieldValue("12345")) }
    var phone by remember { mutableStateOf(TextFieldValue("01799513737")) }
    var firstName by remember { mutableStateOf(TextFieldValue("Test")) }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    suspend fun handleRegistration() {
        try {
            loading = false
            errorMessage = ""
            val res = ApiService.register(firstName.text, email.text, password.text)
            println(res)
//            val userId = res?.data?.sessionId
//            if (!userId.isNullOrEmpty()) {
//                toastState = toastState.copy(
//                    isOpen = true,
//                    isSuccess = true,
//                    message = "Successfully logged user."
//                )
//
//            }
        } catch (ex: Exception) {
            println(ex)
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
            title = "Registration"
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
                            text = "Create an account.",
                            color = Color.Black,
                            fontSize = 28.sp,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            text = "New account account.",
                            color = Color.Gray,
                            fontSize = 16.sp,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                        )
                    }

                    Spacer(Modifier.height(20.dp))

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


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {

                        Column {
                            Text(
                                text = "Already have an Account?",
                                color = Color.DarkGray,
                                fontSize = 14.sp,
                            )
                            Text(
                                text = "Login here",
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
                        label = "Submit",
                        onClick = {
                            coroutineScope.launch {
                                handleRegistration()
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
