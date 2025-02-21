package com.rs.rsauthenticator.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileOutputStream
import com.rs.rsauthenticator.database.AppStateDbHelper
import com.rs.rsauthenticator.dto.AuthenticatorEntry
import com.rs.rsauthenticator.state.AccountState

class BackupManager(private val context: Context) {

    private val filePath: String = context.filesDir.absolutePath + "/backup_rs_authenticator.json"

    fun <T> backup(item: T): BackupManager {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonData = gson.toJson(item)

        val file = File(filePath)
        FileOutputStream(file).use { outputStream ->
            outputStream.write(jsonData.toByteArray())
        }
        return this
    }


    fun download() {
        val file = File(filePath)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/json"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share Backup"))
    }

    fun restore(items: List<AuthenticatorEntry>, dbHelper: AppStateDbHelper) {
        items.forEach {
            val item = dbHelper.findOneBySecret(it.secret)
            if (item?.secret.isNullOrEmpty()) {
                val newOtp = generateTOTP(it.secret, it.algorithm)
                val newItem = AuthenticatorEntry(
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
                    newOtp = newOtp,
                    remainingTime = 0F,
                    createdAt = it.createdAt
                )

                val lastId = dbHelper.insertTotpEntry(newItem)
                if (lastId != "") {
                    AccountState.insertItem(newItem)
                }
            }
        }
    }
}
