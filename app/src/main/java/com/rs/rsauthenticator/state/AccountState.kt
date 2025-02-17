package com.rs.rsauthenticator.state

import androidx.compose.runtime.*
import com.rs.rsauthenticator.database.TotpDatabaseHelper
import com.rs.rsauthenticator.dto.AuthenticatorEntry
import com.rs.rsauthenticator.utils.generateTOTP

object AccountState {
    private var _items by mutableStateOf<List<AuthenticatorEntry>>(emptyList())
    val items: List<AuthenticatorEntry> get() = _items

    fun loadItems(dbHelper: TotpDatabaseHelper) {
        _items = dbHelper.getAllTotpEntries().map { account ->
            AuthenticatorEntry(
                issuer = account.issuer,
                accountName = account.accountName,
                otpCode = generateTOTP(account.secret, account.algorithm),
                remainingTime = account.remainingTime?.toLong() ?: 0, // getRemainingTime()
                secret = account.secret,
                id = account.id,
                logoUrl = account.logoUrl ?: "",
                createdAt = account.createdAt,
                algorithm = account.algorithm
            )
        }
    }

    fun updateAll(items: List<AuthenticatorEntry>) {
        _items = items
    }

    fun insertItem(entry: AuthenticatorEntry) {
//        val newOtp = generateTOTP(entry.secret)
//        dbHelper.insertTotpEntry(entry, newOtp, 30F)
        _items = _items + entry
    }

    fun removeItem(dbHelper: TotpDatabaseHelper, id: String) {
        dbHelper.deleteTotpEntry(id)
        _items = _items.filterNot { it.id == id }
    }
}
