package com.rs.rsauthenticator.state

import androidx.compose.runtime.*
import com.rs.rsauthenticator.database.AppStateDbHelper
import com.rs.rsauthenticator.dto.AuthenticatorEntry
import com.rs.rsauthenticator.utils.generateTOTP

object AccountState {
    private var _items by mutableStateOf<List<AuthenticatorEntry>>(emptyList())
    val items: List<AuthenticatorEntry> get() = _items

    fun loadItems(dbHelper: AppStateDbHelper) {
        _items = dbHelper.getAllTotpEntries().map { account ->
            AuthenticatorEntry(
                issuer = account.issuer,
                accountName = account.accountName,
                newOtp = generateTOTP(account.secret, account.algorithm),
                remainingTime = account.remainingTime,
                secret = account.secret,
                id = account.id,
                logoUrl = account.logoUrl ?: "",
                createdAt = account.createdAt,
                algorithm = account.algorithm,
                protocol = account.protocol,
                digits = account.digits,
                type = account.type,
                period = account.period
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

    fun removeItem(dbHelper: AppStateDbHelper, secret: String) {
        dbHelper.deleteTotpEntry(secret)
        _items = _items.filterNot { it.secret == secret }
    }
}
