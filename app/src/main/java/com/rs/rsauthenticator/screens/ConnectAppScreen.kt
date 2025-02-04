package com.rs.rsauthenticator.screens

import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.ScreenHeader


@Composable
fun RequestCameraPermission(onPermissionGranted: () -> Unit) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onPermissionGranted()
            } else {
                Toast.makeText(context, "Camera permission required!", Toast.LENGTH_SHORT).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        launcher.launch(Manifest.permission.CAMERA)
    }
}


@Composable
fun ConnectAppScreen(applicationContext: Context, navController: NavHostController) {

    RequestCameraPermission(onPermissionGranted = {})

    var isScanned by remember { mutableStateOf(false) }
    var code by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFEF4720), Color(0xFF89A5CE))
                )
            )
    ) {

        ScreenHeader(modifier = Modifier, navigate = {
            navController.navigate("home")
        }, title = "Back")


        RsColumn(modifier = Modifier, px = 16.dp) {
            RsColumn(modifier = Modifier, pt = 90.dp) {
                RsColumn(modifier = Modifier) {
                    CustomText(
                        icon = "\uf0c1",
                        fs = 40.sp,
                        color = Color.Blue,
                        fontWeight = FontWeight.Normal
                    )
                }

                RsColumn(pt = 30.dp) {
                    CustomText(
                        text = "Add an Authenticator Code.",
                        fs = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

        }

        if (isScanned) {


        } else {
//            ScanQRCodeScreen(navController, onQRCodeScanned = {
//                isScanned = true
//                code = it
//            })
        }
    }
}
