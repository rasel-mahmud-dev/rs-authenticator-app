package com.rs.rsauthenticator.state

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.rs.rsauthenticator.database.TotpDatabaseHelper
import com.rs.rsauthenticator.dto.AuthenticatorEntry
import com.rs.rsauthenticator.screens.generateTOTP

object AccountState {
    private var _items by mutableStateOf<List<AuthenticatorEntry>>(emptyList())
    val items: List<AuthenticatorEntry> get() = _items

    fun loadItems(dbHelper: TotpDatabaseHelper) {
        _items = dbHelper.getAllTotpEntries().map { account ->
            AuthenticatorEntry(
                issuer = account.issuer,
                accountName = account.accountName,
                otpCode = generateTOTP(account.secret),
                remainingTime = 30f, // getRemainingTime()
                secret = account.secret,
                id = account.accountName,
                logoUrl = account.logoUrl ?: ""
            )
        }
    }

    fun insertItem(dbHelper: TotpDatabaseHelper, entry: AuthenticatorEntry) {
//        dbHelper.insertTotpEntry(entry)
        _items = _items + entry
    }
}
