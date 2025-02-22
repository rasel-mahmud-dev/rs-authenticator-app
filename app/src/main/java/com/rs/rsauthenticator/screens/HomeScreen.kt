package com.rs.rsauthenticator.screens

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
import com.rs.rsauthenticator.database.AppStateDbHelper
import com.rs.rsauthenticator.dto.AuthenticatorEntry
import com.rs.rsauthenticator.screens.TotpOtp.TokenScreen
import com.rs.rsauthenticator.screens.common.RequestCameraPermission
import com.rs.rsauthenticator.screens.scanQR.ScanQRCodeScreen
import com.rs.rsauthenticator.state.AccountState
import com.rs.rsauthenticator.ui.providers.LocalToastController
import com.rs.rsauthenticator.utils.generateTOTP
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen2(navController: NavHostController) {

    val applicationContext = LocalContext.current

    val dbHelper = AppStateDbHelper.getInstance(applicationContext)
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var isScanCompleted by remember { mutableStateOf(false) }

//    otpauth://totp/rasel.mahmud.dev?secret=GEC7FRTCQRTFQIU6DNDZZWWB4A46FFBW&digits=6&issuer=Facebook
//    var scannedCode by remember { mutableStateOf("otpauth://totp/RsAuth%7Chttps://play-lh.googleusercontent.com/DTzWtkxfnKwFO3ruybY1SKjJQnLYeuK3KmQmwV5OQ3dULr5iXxeEtzBLceultrKTIUTr:rasel@gmail.com?algorithm=SHA256&digits=6&issuer=RsAuth%7Chttps:%2F%2Fplay-lh.googleusercontent.com%2FDTzWtkxfnKwFO3ruybY1SKjJQnLYeuK3KmQmwV5OQ3dULr5iXxeEtzBLceultrKTIUTr&period=30&secret=V364VS7WUNHR4UJA3JEB4MVSNNFYSPYL") }
//    val a = "otpauth://totp/RsAuthenticatorWeb:test@gmail.com%7Chttps://play-lh.googleusercontent.com/DTzWtkxfnKwFO3ruybY1SKjJQnLYeuK3KmQmwV5OQ3dULr5iXxeEtzBLceultrKTIUTr?algorithm=SHA256&digits=6&issuer=RsAuthenticatorWeb&period=30&secret=EC7IJYYDQTEKIITXMVSJ7JJZJPKTMHYG"
//    val a = "otpauth://totp/rasel.mahmud.dev?secret=GEC7FRTCQRTFQIU6DNDZZWWB4A46FFBW&digits=6&issuer=Facebook"
    val a = "otpauth://totp/GitHub:rasel-mahmud-dev?secret=PUXJCCF4ULPYDI4U&issuer=GitHub"
//    val a = "otpauth://totp/RsAuthenticatorWeb2:test@gmail.com%7Chttps://play-lh.googleuse rcontent.com/DTzWtkxfnKwFO3ruybY1SKjJQnLYeuK3KmQmwV5OQ3dULr5iXxeEtzBLceultrKTIUTr?algorithm=SHA256&digits=6&issuer=RsAuthenticatorWeb2&period=30&secret=6VMGGTYH2ZLNRT23WKS4GVEPSTMOTDTK"

    var scannedCode by remember { mutableStateOf("") }

    val toastController = LocalToastController.current


    fun handleAddApp(scannedCode: String) {
        val totpData = AuthenticatorEntry.fromUri(scannedCode)
        totpData?.let {

            println(scannedCode)

            if (it.secret.isNotEmpty() && it.issuer.isNotEmpty()) {
                val item = dbHelper.findOneBySecret(it.secret)
                if (!item?.secret.isNullOrEmpty()) {
                    toastController.showToast(message = "Already linked.", isSuccess = true, 1000)
                } else {
                    val newOtp = generateTOTP(it.secret, it.algorithm)
                    it.newOtp = newOtp

                    val lastId = dbHelper.insertTotpEntry(it)
                    AccountState.insertItem(it)

                    toastController.showToast(
                        message = "Successfully account added.",
                        isSuccess = true
                    )
                }
            } else {
                toastController.showToast(
                    message = "QR code unknown or invalid.",
                    isSuccess = false,
                    timeout = 3000
                )
            }
        }
    }


    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        RsColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TokenScreen(navController, onShowBottomSheet = {
//                handleAddApp(a)
                showBottomSheet = true
                isScanCompleted = false
            })
        }

        if (showBottomSheet) {
            RsBottomSheet(
                sheetState = sheetState,
                onClose = {
                    showBottomSheet = false
                    isScanCompleted = true
                },
                backgroundColor = Color(0xFFFFFFFF),
            ) {

                RsColumn {
                    RequestCameraPermission(onPermissionGranted = {})

                    ScanQRCodeScreen(navController, onQRCodeScanned = {

                        scope.launch {
                            sheetState.hide()
                            showBottomSheet = false

                            if (!isScanCompleted) {
                                handleAddApp(it)
                            }
                            isScanCompleted = true
                            sheetState.hide()
                            showBottomSheet = false
                        }
                    }, onClose = {
                        scope.launch {
                            sheetState.hide()
                            showBottomSheet = false
                            isScanCompleted = true
                        }
                    })
                }
            }
        }
    }
}

