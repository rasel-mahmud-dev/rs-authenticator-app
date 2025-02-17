package com.rs.rsauthenticator.screens.Settings


import android.content.Context
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
import com.google.gson.reflect.TypeToken
import com.rs.rsauthenticator.components.CustomText
import com.rs.rsauthenticator.components.PrimaryButton
import com.rs.rsauthenticator.components.RsColumn
import com.rs.rsauthenticator.components.RsRow
import com.rs.rsauthenticator.components.ScreenHeader
import com.rs.rsauthenticator.components.Toast
import com.rs.rsauthenticator.components.ToastState
import com.rs.rsauthenticator.database.TotpDatabaseHelper
import com.rs.rsauthenticator.dto.AuthenticatorEntry
import com.rs.rsauthenticator.dto.TotpUriData
import com.rs.rsauthenticator.state.AccountState
import com.rs.rsauthenticator.ui.theme.Primary40
import com.rs.rsauthenticator.ui.theme.Purple40
import com.rs.rsauthenticator.utils.BackupManager
import com.rs.rsauthenticator.utils.generateTOTP
import getInputStreamFromUri
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.InputStreamReader


@Composable
fun BackupRestore(applicationContext: Context, navHostController: NavHostController) {

    val context = LocalContext.current
    val backupManager = BackupManager(context)

    val coroutineScope = rememberCoroutineScope()

    val dbHelper = TotpDatabaseHelper.getInstance(applicationContext)


    var toastState by remember {
        mutableStateOf(
            ToastState(
                isOpen = false,
                isSuccess = false,
                message = ""
            )
        )
    }

    val pickFileLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val inputStream = getInputStreamFromUri(context, uri)
                inputStream?.let {
                    val inputStreamReader = InputStreamReader(inputStream)
                    val gson = Gson()

                    val type = object : TypeToken<List<AuthenticatorEntry>>() {}.type
                    val restoredEntries: List<AuthenticatorEntry> =
                        gson.fromJson(inputStreamReader, type)

                    restoredEntries.forEach {
                        val item = dbHelper.findOneBySecret(it.secret)
                        if (!item?.secret.isNullOrEmpty()) {
                            toastState = toastState.copy(
                                isOpen = true,
                                isSuccess = true,
                                message = "Already linked ${it.accountName} on ${it.issuer}."
                            )
                        } else {

                            val newOtp = generateTOTP(it.secret, it.algorithm)
                            val lastId = dbHelper.insertTotpEntry(
                                TotpUriData(
                                    id = it.id,
                                    protocol = "otpauth",
                                    type = "",
                                    accountName = it.accountName,
                                    secret = it.secret,
                                    issuer = it.issuer,
                                    algorithm = it.algorithm,
                                    digits = 6,
                                    period = 30,
                                    logoUrl = it.logoUrl,
                                    newOtp = "",
                                    remainingTime = 0F,
                                    createdAt = it.createdAt
                                ), newOtp, 0F
                            )

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
                        }
                    }

                    toastState = toastState.copy(
                        isOpen = true,
                        isSuccess = true,
                        message = "Restored Entries."
                    )

                    coroutineScope.launch {
                        delay(2000)
                        toastState = toastState.copy(isOpen = false)
                        navHostController.navigate("home")
                    }

                }

            } ?: run {
                println("Error opening file or file is empty")
            }
        }

    fun handleBackup() {
        backupManager
            .backup(AccountState.items)
            .download()
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

                Toast(modifier = Modifier, toastState = toastState)

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

