package com.rs.rsauthenticator.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rs.rsauthenticator.components.HomeBottomNav
import com.rs.rsauthenticator.components.PrimaryButton
import com.rs.rsauthenticator.components.RsBottomSheet
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.SettingScreen
import com.rs.rsauthenticator.database.TotpDatabaseHelper
import com.rs.rsauthenticator.dto.AuthenticatorEntry
import com.rs.rsauthenticator.dto.TotpUriData
import com.rs.rsauthenticator.state.AccountState

import kotlinx.coroutines.launch


fun generateTOTP(secret: String): String {


//    val secret = "your-secret-key"
//    val otp = Totp().generate(secret.toByteArray())

    return (Math.random() * 1000).toString()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen2(applicationContext: Context, navController: NavHostController) {

    var activeTab by remember { mutableStateOf("tokens") }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var scannedCode by remember { mutableStateOf("otpauth://totp/RsAuth%7Chttps://play-lh.googleusercontent.com/DTzWtkxfnKwFO3ruybY1SKjJQnLYeuK3KmQmwV5OQ3dULr5iXxeEtzBLceultrKTIUTr:rasel@gmail.com?algorithm=SHA256&digits=6&issuer=RsAuth%7Chttps:%2F%2Fplay-lh.googleusercontent.com%2FDTzWtkxfnKwFO3ruybY1SKjJQnLYeuK3KmQmwV5OQ3dULr5iXxeEtzBLceultrKTIUTr&period=30&secret=V364VS7WUNHR4UJA3JEB4MVSNNFYSPYL") }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        activeTab = "tokens"
    }

    fun handleAddApp() {
        val totpData = TotpUriData.fromUri(scannedCode)
        totpData?.let {
            val dbHelper = TotpDatabaseHelper.getInstance(applicationContext)
            val newOtp = generateTOTP(it.secret)
            dbHelper.insertTotpEntry(it, newOtp, 30F)
            AccountState.insertItem(
                dbHelper, AuthenticatorEntry(
                    id = it.id,
                    issuer = it.issuer,
                    remainingTime = 30F,
                    logoUrl = it.logoUrl ?: "",
                    accountName = it.accountName,
                    secret = it.secret,
                    otpCode = newOtp,
                )
            )
        }
    }

    RsColumn(
        modifier = Modifier.fillMaxSize(),
        bgColor = (Color(0xFF1A1A1A)),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Box(
            modifier = Modifier
                .weight(1F)
                .padding(10.dp, 0.dp),
        ) {
            if (activeTab == "tokens") {

                TokenScreen(navController)

                RsColumn(Modifier.align(Alignment.BottomEnd)) {
                    PrimaryButton(
                        onClick = {
//                            showBottomSheet = true

                            handleAddApp()
                        },
                        modifier = Modifier.padding(3.dp),
                        label = "New Connection",
                        iconSize = 18.sp,
                        icon = "\u002b",
                    )
                }

            } else {
                SettingScreen(applicationContext, navController)
            }
        }


        RsColumn(modifier = Modifier) {
            HomeBottomNav(activeTab = activeTab, onChangeTab = { activeTab = it })
        }


        if (showBottomSheet) {
            RsBottomSheet(
                sheetState = sheetState,
                onClose = { showBottomSheet = false },
                backgroundColor = Color(0xFF252633),
            ) {

                RsColumn {
                    RequestCameraPermission(onPermissionGranted = {})

                    ScanQRCodeScreen(navController, onQRCodeScanned = {
                        scannedCode = it
                        scope.launch {
                            sheetState.hide()
                            showBottomSheet = false
                        }
                    }, onClose = {
                        scope.launch {
                            sheetState.hide()
                            showBottomSheet = false
                        }
                    })
                }
            }
        }
    }
}

