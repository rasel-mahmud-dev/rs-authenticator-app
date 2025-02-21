package com.rs.rsauthenticator.screens.security

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.RsIconButton
import com.rs.rsauthenticator.components.RsRow
import com.rs.rsauthenticator.components.ScreenHeader
import com.rs.rsauthenticator.components.unlock.PinKeyboard
import com.rs.rsauthenticator.database.AppStateDbHelper
import com.rs.rsauthenticator.state.AppState
import com.rs.rsauthenticator.ui.theme.AppColors
import com.rs.rsauthenticator.ui.theme.faSolid


@Composable
fun PinSetupScreen(navHostController: NavHostController) {

    val context = LocalContext.current
    val db = AppStateDbHelper.getInstance(context)

    fun handleSetupPin(pin: String) {
        db.savePin(pin)
        navHostController.navigate("settings/security")
    }

    Scaffold(topBar = {
        ScreenHeader(title = "Setup Pin", navigate = { navHostController.popBackStack() })
    }, content = { padding ->
        Column(
            modifier = Modifier.padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            RsIconButton(w = 80.dp, h = 80.dp, bgColor = AppColors.Primary40, radius = 20.dp) {
                CustomText(
                    icon = "\ue423", fontFamily = faSolid, color = Color.White, fs = 35.sp
                )
            }
            Spacer(modifier = Modifier.height(36.dp))
            CustomText(
                text = "Set up PIN",
                color = AppColors.Dark40,
                fs = 20.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(16.dp))

            PinKeyboard {
                handleSetupPin(it)
            }
        }
    })
}


@Preview
@Composable
fun PreviewPinSetupScreen() {
    val navController = NavHostController(context = LocalContext.current)
    PinSetupScreen(navController)
}

