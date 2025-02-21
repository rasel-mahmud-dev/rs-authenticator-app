package com.rs.rsauthenticator.screens.settings


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.PrimaryButton
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.RsRow
import com.rs.rsauthenticator.components.ScreenHeader
import com.rs.rsauthenticator.database.AppStateDbHelper
import com.rs.rsauthenticator.dto.AuthenticatorEntry
import com.rs.rsauthenticator.ui.providers.LocalToastController
import com.rs.rsauthenticator.ui.theme.Purple40
import com.rs.rsauthenticator.utils.BackupManager
import com.rs.rsauthenticator.utils.generateTOTP
import getInputStreamFromUri
import java.io.InputStreamReader


@Composable
fun BackupRestore(navHostController: NavHostController) {

    val applicationContext = LocalContext.current
    val toastController = LocalToastController.current

    val context = LocalContext.current
    val backupManager = BackupManager(context)

    val dbHelper = AppStateDbHelper.getInstance(applicationContext)

    fun restoreItems(items: List<AuthenticatorEntry>) {
        if (items.isEmpty()) {
            toastController.showToast(
                message = "Error: No entries to restore.",
                isSuccess = false,
                timeout = 3000
            )
            return
        }
        try {
            backupManager.restore(items, dbHelper)
            toastController.showToast(
                message = "Restored Entries.",
                isSuccess = true,
                timeout = 3000
            )
            navHostController.navigate("home")
        } catch (ex: Exception){
            toastController.showToast(
                message = "Unexpected error: ${ex.message}",
                isSuccess = false,
                timeout = 3000
            )
            ex.printStackTrace()
        }
    }


    val pickFileLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            try {
                if (uri == null) {
                    toastController.showToast("Error: No file selected", false)
                    return@rememberLauncherForActivityResult
                }

                val inputStream = getInputStreamFromUri(context, uri)
                if (inputStream == null) {
                    toastController.showToast("Error: Unable to open file", false)
                    return@rememberLauncherForActivityResult
                }

                val inputStreamReader = InputStreamReader(inputStream)
                val gson = Gson()
                val type = object : TypeToken<List<AuthenticatorEntry>>() {}.type

                val restoredEntries: List<AuthenticatorEntry> = try {
                    gson.fromJson(inputStreamReader, type) ?: emptyList()
                } catch (e: JsonSyntaxException) {
                    toastController.showToast("Error: Invalid file format", false)
                    emptyList()
                }

                if (restoredEntries.isEmpty()) {
                    toastController.showToast("Error: File is empty", false)
                    return@rememberLauncherForActivityResult
                }

                restoreItems(restoredEntries)

            } catch (e: Exception) {
                toastController.showToast("Unexpected error: ${e.message}", false, 2000)
            }
        }


    fun handleBackup() {
        try {
            val items = dbHelper.getAllTotpEntries()
            backupManager
                .backup(items)
                .download()
            toastController.showToast("Backup process completed.", true, 2000)
        } catch (ex: Exception) {
            toastController.showToast("Unable to complete backup process.", false, 2000)
        }
    }

    fun handleRestore() {
        pickFileLauncher.launch("application/json")
    }

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

                    CustomText(
                        text = "Easily backup and restore your data. Deleted items will appear here, and you can restore them within 30 days before they are permanently deleted.",
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium,
                        fs = 16.sp,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.alpha(0.9f),
                        textAlign = TextAlign.Center
                    )

                    RsRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        PrimaryButton(
                            onClick = { handleRestore() },
                            modifier = Modifier
                                .weight(1F),
                            label = "Import",
                            icon = "\uf56f"
                        )

                        PrimaryButton(
                            bgColor = Purple40,
                            onClick = { handleBackup() },
                            modifier = Modifier
                                .weight(1F),
                            label = "Export",
                            icon = "\uf56e"
                        )
                    }
                }
            }
        }
    )
}

