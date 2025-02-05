package com.rs.rsauthenticator.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.rs.rsauthenticator.components.AuthenticatorItem
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.HomeBottomNav
import com.rs.rsauthenticator.components.PrimaryButton
import com.rs.rsauthenticator.components.RsBottomSheet
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.SettingScreen
import com.rs.rsauthenticator.database.StateDatabaseHelper
import com.rs.rsauthenticator.database.TotpDatabaseHelper
import com.rs.rsauthenticator.dto.AuthenticatorEntry
import com.rs.rsauthenticator.dto.TotpUriData
import kotlinx.coroutines.launch


fun generateTOTP(secret: String): String {


//    val secret = "your-secret-key"
//    val otp = Totp().generate(secret.toByteArray())

    return (Math.random() * 1000).toString()

}


fun getRemainingTime(): Float {
    val timeStep = 30
    val currentTime = System.currentTimeMillis() / 1000
    return 1f - (currentTime % timeStep) / timeStep.toFloat()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen2(applicationContext: Context, navController: NavHostController) {

    var activeTab by remember { mutableStateOf("tokens") }
    val dbHelper = TotpDatabaseHelper.getInstance(applicationContext)

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var scannedCode by remember { mutableStateOf("otpauth://totp/RsAuth%7Chttps://play-lh.googleusercontent.com/DTzWtkxfnKwFO3ruybY1SKjJQnLYeuK3KmQmwV5OQ3dULr5iXxeEtzBLceultrKTIUTr:rasel@gmail.com?algorithm=SHA256&digits=6&issuer=RsAuth%7Chttps:%2F%2Fplay-lh.googleusercontent.com%2FDTzWtkxfnKwFO3ruybY1SKjJQnLYeuK3KmQmwV5OQ3dULr5iXxeEtzBLceultrKTIUTr&period=30&secret=V364VS7WUNHR4UJA3JEB4MVSNNFYSPYL") }

    val scope = rememberCoroutineScope()

    fun getStoredAccounts(): List<AuthenticatorEntry> {
        val entries = mutableListOf<AuthenticatorEntry>()

        val storedAccounts = dbHelper.getAllTotpEntries()
        for (account in storedAccounts) {
            val secret = account.secret
            val logoUrl = account.logoUrl ?: ""
            val otpCode = generateTOTP(secret)
            val remainingTime = 30f // getRemainingTime()

            entries.add(
                AuthenticatorEntry(
                    issuer = account.issuer,
                    accountName = account.accountName,
                    otpCode = otpCode,
                    remainingTime = remainingTime,
                    secret = secret,
                    id = account.accountName,
                    logoUrl = logoUrl
                )
            )
        }
        return entries
    }

    val entries = remember { getStoredAccounts() }


    LaunchedEffect(Unit) {
        activeTab = "tokens"
    }


    fun handleAddApp() {
//        val totpData = TotpUriData.fromUri(scannedCode)
//        totpData?.let {
//            val dbHelper = TotpDatabaseHelper.getInstance(applicationContext)
//            dbHelper.insertTotpEntry(it)
//        }
    }

    RsColumn(
        modifier = Modifier.fillMaxSize(),
        bgColor = (Color(0xFF1A1A1A)),
        verticalArrangement = Arrangement.SpaceBetween
    ) {


        Box(
            modifier = Modifier
                .weight(1F)
                .padding(16.dp),
        ) {
            if (activeTab == "tokens") {

                RsColumn(modifier = Modifier) {

                    RsColumn(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter("https://avatars.githubusercontent.com/u/99707905?v=4"),
                            contentDescription = "Rs Authenticator Logo",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .align(Alignment.CenterHorizontally),
                            contentScale = ContentScale.Crop,
                        )
                    }

                    RsColumn(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CustomText(
                            modifier = Modifier,
                            text = "Rs Authenticator",
                            fs = 14.sp,
                            pt = 5.dp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }


                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(entries) { entry ->
                            AuthenticatorItem(entry)
                        }
                    }
                }

                RsColumn(Modifier.align(Alignment.BottomEnd)) {
                    PrimaryButton(
                        onClick = { showBottomSheet = true },
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

                    ScanQRCodeScreen(navController,
                        onQRCodeScanned = {
                            scannedCode = it
                            scope.launch {
                                sheetState.hide()
                                showBottomSheet = false
                            }
                        },
                        onClose = {
                            scope.launch {
                                sheetState.hide()
                                showBottomSheet = false
                            }
                        }
                    )
                }
            }
        }
    }
}

