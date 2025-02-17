package com.rs.rsauthenticator.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.FileInputStream
import com.google.gson.Gson
import java.io.InputStream

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
}
