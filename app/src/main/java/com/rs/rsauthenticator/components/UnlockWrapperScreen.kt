package com.rs.rsauthenticator.components


import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rs.rsauthenticator.components.form.TextInput
import com.rs.rsauthenticator.database.StateDatabaseHelper
import com.rs.rsauthenticator.screens.MainApp


@Composable
fun UnlockWrapperScreen(stateDatabaseHelper: StateDatabaseHelper, onUnlock: () -> Unit) {
    val context = LocalContext.current
    var isUnlocked by remember { mutableStateOf(false) }
    val storedPin by remember { mutableStateOf(stateDatabaseHelper.getPin()) }

    if (storedPin.isNullOrEmpty()) {
        isUnlocked = true
    }

    if (isUnlocked) {
        MainApp(context)
    } else {
        UnlockScreen(onUnlock = {
            isUnlocked = true
        }, storedPin = storedPin ?: "", context)
    }
}

@Composable
fun UnlockScreen(onUnlock: () -> Unit, storedPin: String, context: Context) {
    var errorMessage by remember { mutableStateOf("") }
    var inputPin by remember { mutableStateOf(TextFieldValue("")) }

    RsColumn(
        pt = 40.dp,
        px = 16.dp,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        RsColumn(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            CustomText(
                icon = "\uf084",
                color = Color.Black,
                fs = 70.sp,
            )

            CustomText(
                text = "Unlock Account",
                color = Color.Black,
                fs = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }


        TextInput(
            value = inputPin,
            onChange = { inputPin = it },
            label = "",
            modifier = Modifier,
            radius = 30.dp,
            keyboardType = KeyboardType.Password,
            placeholder = "Pin"
//                label = { Text("Enter PIN") },
//                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.NumberPassword),
//                visualTransformation = PasswordVisualTransformation(),
//                modifier = Modifier.fillMaxWidth()
        )

        errorMessage?.let {
            Text(text = it, color = Color.Red, fontSize = 14.sp)
        }

        PrimaryButton(
            onClick = {
                if (inputPin.text == storedPin) {
                    onUnlock()
                } else {
                    errorMessage = "Incorrect PIN.ff"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            label = "Unlock"
        )
    }

}
