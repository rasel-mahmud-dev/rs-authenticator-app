package com.rs.rsauthenticator.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.rs.rsauthenticator.components.HomeBottomNav
import com.rs.rsauthenticator.components.RsBottomSheet
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.Settings.SettingScreen
import com.rs.rsauthenticator.components.Toast
import com.rs.rsauthenticator.components.ToastState
import com.rs.rsauthenticator.database.TotpDatabaseHelper
import com.rs.rsauthenticator.dto.AuthenticatorEntry
import com.rs.rsauthenticator.dto.TotpUriData
import com.rs.rsauthenticator.state.AccountState
import com.rs.rsauthenticator.utils.generateTOTP
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen2(applicationContext: Context, navController: NavHostController) {

    var activeTab by remember { mutableStateOf("tokens") }
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var scannedCode by remember { mutableStateOf("otpauth://totp/RsAuth%7Chttps://play-lh.googleusercontent.com/DTzWtkxfnKwFO3ruybY1SKjJQnLYeuK3KmQmwV5OQ3dULr5iXxeEtzBLceultrKTIUTr:rasel@gmail.com?algorithm=SHA256&digits=6&issuer=RsAuth%7Chttps:%2F%2Fplay-lh.googleusercontent.com%2FDTzWtkxfnKwFO3ruybY1SKjJQnLYeuK3KmQmwV5OQ3dULr5iXxeEtzBLceultrKTIUTr&period=30&secret=V364VS7WUNHR4UJA3JEB4MVSNNFYSPYL") }

    var toastState by remember {
        mutableStateOf(
            ToastState(
                isOpen = false,
                isSuccess = false,
                message = ""
            )
        )
    }


    fun handleAddApp(scannedCode: String) {
        val totpData = TotpUriData.fromUri(scannedCode)
        totpData?.let {

            if (it.secret.isNotEmpty() && it.issuer.isNotEmpty()) {
                val dbHelper = TotpDatabaseHelper.getInstance(applicationContext)

                val newOtp = generateTOTP(it.secret)
                val lastId = dbHelper.insertTotpEntry(it, newOtp, 30F)

                AccountState.insertItem(
                    dbHelper,
                    AuthenticatorEntry(
                        id = lastId,
                        issuer = it.issuer,
                        remainingTime = 30F,
                        logoUrl = it.logoUrl ?: "",
                        accountName = it.accountName,
                        secret = it.secret,
                        otpCode = newOtp,
                    )
                )


                toastState = toastState.copy(
                    isOpen = true,
                    isSuccess = true,
                    message = "Successfully account added."
                )


            } else {
                toastState = toastState.copy(
                    isOpen = true,
                    isSuccess = false,
                    message = "QR code unknown or invalid."
                )
            }

            CoroutineScope(Dispatchers.Main).launch {
                delay(2000)
                toastState = toastState.copy(
                    isOpen = false,
                )
            }

        }
    }


    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        activeTab = "tokens"
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Toast(modifier = Modifier, toastState = toastState)


        RsColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            if (activeTab == "tokens") {
                TokenScreen(navController, scannedCode, onShowBottomSheet = {
                    showBottomSheet = true
                })


            } else {
                SettingScreen(applicationContext, navController)
            }
        }


        RsColumn(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
        ) {
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
                        handleAddApp(it)
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

