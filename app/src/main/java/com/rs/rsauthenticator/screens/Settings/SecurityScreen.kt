package com.rs.rsauthenticator.screens.Settings

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.rs.rsauthenticator.components.ScreenHeader
import com.rs.rsauthenticator.components.form.TextInput
import com.rs.rsauthenticator.database.StateDatabaseHelper

@Composable
fun SecurityScreen(applicationContext: Context, navHostController: NavHostController) {
    val db = StateDatabaseHelper.getInstance(applicationContext)

    var pin by remember { mutableStateOf(TextFieldValue("")) }
    var confirmPin by remember { mutableStateOf(TextFieldValue("")) }
    var savedPin by remember { mutableStateOf(db.getPin()) }
    var step by remember { mutableIntStateOf(1) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    RsColumn {
        ScreenHeader(title = "Back", navigate = {
            navHostController.popBackStack()
        })

        RsColumn(
            pt = 40.dp,
            px = 16.dp,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomText(
                text = when {
                    step == 1 -> "Set Your PIN"
                    else -> "Confirm Your PIN"
                },
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fs = 24.sp,
                style = MaterialTheme.typography.headlineMedium
            )

            TextInput(
                value = if (step == 1) pin else confirmPin,
                onChange = { if (step == 1) pin = it else confirmPin = it },
                label = if (step == 1) "Enter PIN" else "Confirm PIN",
                modifier = Modifier,
                radius = 30.dp,
                keyboardType = KeyboardType.Password,
                placeholder = if (step == 1) "Enter PIN" else "Confirm PIN"
            )

            errorMessage?.let {
                Text(text = it, color = Color.Red, fontSize = 14.sp)
            }

            PrimaryButton(
                onClick = {
                    if (step == 1) {
                        if (pin.text.length < 4) {
                            errorMessage = "PIN must be at least 4 digits"
                        } else {
                            errorMessage = null
                            step = 2
                        }
                    } else {
                        if (confirmPin.text == pin.text) {
                            db.savePin(pin.text)
                            savedPin = pin.text
                            errorMessage = null
                            navHostController.navigate("home")
                        } else {
                            errorMessage = "PINs do not match, try again."
                        }
                    }

                },
                modifier = Modifier.fillMaxWidth(),
                label = when {
                    step == 1 -> "Next"
                    else -> "Set PIN"
                }
            )
        }
    }
}
