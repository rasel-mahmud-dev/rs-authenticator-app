package com.rs.rsauthenticator.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.PrimaryButton
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.ScreenHeader
import com.rs.rsauthenticator.ui.theme.Primary40

@Composable
fun TrashScreen(applicationContext: Context, navHostController: NavHostController) {
    var showContent by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            ScreenHeader(
                title = "Trash",
                navigate = { navHostController.popBackStack() }
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                RsColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp)
                ) {
                    CustomText(
                        icon = "\u0000",
                        modifier = Modifier.size(80.dp)
                    )

                    CustomText(
                        text = "Your Trash is Empty",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fs = 22.sp,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    AnimatedVisibility(visible = showContent) {
                        CustomText(
                            text = "Deleted items will appear here. You can restore them within 30 days before they are permanently removed.",
                            color = Color.Gray,
                            fontWeight = FontWeight.Normal,
                            fs = 16.sp,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.alpha(0.8f)
                        )
                    }

                    PrimaryButton(
                        onClick = { showContent = !showContent },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        label = if (showContent) "Hide Info" else "Show Info",
                    )

                    RsColumn(pt = 50.dp, horizontalAlignment = Alignment.CenterHorizontally) {
                        CustomText(
                            icon = "\uf06a",
                            color = Primary40,
                            fontWeight = FontWeight.SemiBold,
                            fs = 50.sp,
                            textAlign = TextAlign.Center
                        )

                        CustomText(
                            pt = 20.dp,
                            text = "The Trash feature is coming soon! Stay tuned for updates.",
                            color = Primary40,
                            fontWeight = FontWeight.SemiBold,
                            fs = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                }
            }
        }
    )
}
