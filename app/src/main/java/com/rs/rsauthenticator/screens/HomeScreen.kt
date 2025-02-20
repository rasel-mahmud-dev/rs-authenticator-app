package com.rs.rsauthenticator.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.rs.rsauthenticator.components.RsBottomSheet
import com.rs.rsauthenticator.components.RsColumn
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
fun HomeScreen2(navController: NavHostController) {

    val applicationContext = LocalContext.current

    val dbHelper = TotpDatabaseHelper.getInstance(applicationContext)
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

//    otpauth://totp/rasel.mahmud.dev?secret=GEC7FRTCQRTFQIU6DNDZZWWB4A46FFBW&digits=6&issuer=Facebook
//    var scannedCode by remember { mutableStateOf("otpauth://totp/RsAuth%7Chttps://play-lh.googleusercontent.com/DTzWtkxfnKwFO3ruybY1SKjJQnLYeuK3KmQmwV5OQ3dULr5iXxeEtzBLceultrKTIUTr:rasel@gmail.com?algorithm=SHA256&digits=6&issuer=RsAuth%7Chttps:%2F%2Fplay-lh.googleusercontent.com%2FDTzWtkxfnKwFO3ruybY1SKjJQnLYeuK3KmQmwV5OQ3dULr5iXxeEtzBLceultrKTIUTr&period=30&secret=V364VS7WUNHR4UJA3JEB4MVSNNFYSPYL") }
//    val a = "otpauth://totp/RsAuthenticatorWeb:test@gmail.com%7Chttps://play-lh.googleusercontent.com/DTzWtkxfnKwFO3ruybY1SKjJQnLYeuK3KmQmwV5OQ3dULr5iXxeEtzBLceultrKTIUTr?algorithm=SHA256&digits=6&issuer=RsAuthenticatorWeb&period=30&secret=EC7IJYYDQTEKIITXMVSJ7JJZJPKTMHYG"
//    val a = "otpauth://totp/rasel.mahmud.dev?secret=GEC7FRTCQRTFQIU6DNDZZWWB4A46FFBW&digits=6&issuer=Facebook"
//    val a = "otpauth://totp/GitHub:rasel-mahmud-dev?secret=PUXJCCF4ULPYDI4U&issuer=GitHub"

    var scannedCode by remember { mutableStateOf("") }
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

            println(scannedCode)
            if (it.secret.isNotEmpty() && it.issuer.isNotEmpty()) {

                val item = dbHelper.findOneBySecret(it.secret)
                if (!item?.secret.isNullOrEmpty()) {
                    toastState = toastState.copy(
                        isOpen = true,
                        isSuccess = true,
                        message = "Already linked."
                    )
                } else {
                    val newOtp = generateTOTP(it.secret, it.algorithm)
                    val lastId = dbHelper.insertTotpEntry(it, newOtp, 0F)

                    AccountState.insertItem(
                        AuthenticatorEntry(
                            id = lastId,
                            issuer = it.issuer,
                            remainingTime = System.currentTimeMillis() + 30 * 1000,
                            logoUrl = it.logoUrl ?: "",
                            accountName = it.accountName,
                            secret = it.secret,
                            otpCode = newOtp,
                            algorithm = it.algorithm,
                            createdAt = System.currentTimeMillis()
                        )
                    )

                    toastState = toastState.copy(
                        isOpen = true,
                        isSuccess = true,
                        message = "Successfully account added."
                    )
                }

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


    Box(modifier = Modifier.fillMaxSize()) {

        Toast(modifier = Modifier, toastState = toastState)

        RsColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TokenScreen(navController, onShowBottomSheet = {
                showBottomSheet = true
            })
        }

        if (showBottomSheet) {
            RsBottomSheet(
                sheetState = sheetState,
                onClose = { showBottomSheet = false },
                backgroundColor = Color(0xFFFFFFFF),
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

