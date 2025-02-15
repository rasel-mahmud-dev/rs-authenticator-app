package com.rs.rsauthenticator.screens.Settings


import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import com.rs.rsauthenticator.components.RsRow
import com.rs.rsauthenticator.components.ScreenHeader


@Composable
fun BackupRestore(applicationContext: Context, navHostController: NavHostController) {
    Scaffold(
        topBar = {
            ScreenHeader(
                title = "Backup & Restore",
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
                        .padding(horizontal = 20.dp, vertical = 80.dp)
                ) {

                    CustomText(
                        text = "Backup & Restore",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fs = 24.sp,
                        style = MaterialTheme.typography.headlineLarge
                    )

                    // Description
                    CustomText(
                        text = "Easily backup and restore your data. Deleted items will appear here, and you can restore them within 30 days before they are permanently deleted.",
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium,
                        fs = 16.sp,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.alpha(0.9f),
                        textAlign = TextAlign.Center
                    )

                    // Import & Export Buttons
                    RsRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        PrimaryButton(
                            onClick = { /* Import Logic */ },
                            modifier = Modifier.weight(1F),
                            label = "Import Backup"
                        )

                        PrimaryButton(
                            onClick = { /* Export Logic */ },
                            modifier = Modifier.weight(1F),
                            label = "Export Backup"
                        )
                    }

                    // Coming Soon Section
                    RsColumn(
                        pt = 50.dp,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CustomText(
                            icon = "\uf06a", // Info icon
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            fs = 50.sp,
                            textAlign = TextAlign.Center
                        )

                        CustomText(
                            pt = 20.dp,
                            text = "This feature is under development! Stay tuned for updates.",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium,
                            fs = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}

