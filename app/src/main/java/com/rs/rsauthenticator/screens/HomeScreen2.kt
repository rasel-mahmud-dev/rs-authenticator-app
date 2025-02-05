package com.rs.rsauthenticator.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
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
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.HomeBottomNav
import com.rs.rsauthenticator.components.PrimaryButton
import com.rs.rsauthenticator.components.RsBottomSheet
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.SettingScreen
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen2(applicationContext: Context, navController: NavHostController) {

    var activeTab by remember { mutableStateOf("tokens") }

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var scannedCode by remember { mutableStateOf("") }

    var scope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        activeTab = "tokens"
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

                    RsColumn(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CustomText(
                            textAlign = TextAlign.Center,
                            modifier = Modifier,
                            text = "Rs Authenticator Authenticator  Authenticator  Authenticator sdfsd Authenticator  sd Authenticator",
                            fs = 14.sp,
                            pt = 5.dp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )
                    }


                    RsColumn(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CustomText(
                            textAlign = TextAlign.Center,
                            modifier = Modifier,
                            text = scannedCode,
                            fs = 16.sp,
                            pt = 5.dp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )
                    }
                }

                RsColumn(Modifier.align(Alignment.BottomEnd)) {
                    PrimaryButton(
                        onClick = { showBottomSheet = true },
                        modifier = Modifier.padding(3.dp),
                        label = "New Connection",
                        icon = ""
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

