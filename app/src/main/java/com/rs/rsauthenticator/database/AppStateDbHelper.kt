package com.rs.rsauthenticator.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.core.database.getLongOrNull
import com.google.gson.Gson
import com.rs.rsauthenticator.dto.AuthenticatorEntry
import com.rs.rsauthenticator.state.Auth

class AppStateDbHelper private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ApplicationState.db"
        private const val DATABASE_VERSION = 3

        const val TABLE_STATE = "state"
        private const val COLUMN_KEY = "key"
        private const val COLUMN_VALUE = "value"


        // Table: connected_account_entries
        private const val TABLE_TOTP = "totp_entries"
        private const val COLUMN_ID = "id"
        private const val COLUMN_ACCOUNT_NAME = "account_name"
        private const val COLUMN_SECRET = "secret"
        private const val COLUMN_ISSUER = "issuer"
        private const val COLUMN_ALGORITHM = "algorithm"
        private const val COLUMN_DIGITS = "digits"
        private const val COLUMN_PERIOD = "period"
        private const val COLUMN_LOGO_URL = "logo_url"
        private const val COLUMN_NEW_OTP = "new_otp"
        private const val COLUMN_REMAINING_TIME = "remaining_time"
        private const val COLUMN_CREATED_AT = "created_at"


        @Volatile
        private var instance: AppStateDbHelper? = null

        fun getInstance(context: Context): AppStateDbHelper {

            val dbFile = context.getDatabasePath("ApplicationState.db")
            println("Database path: ${dbFile.absolutePath}, Exists: ${dbFile.exists()}")



            return instance ?: synchronized(this) {
                instance ?: AppStateDbHelper(context.applicationContext).also { instance = it }
            }
        }
    }

    init {
        writableDatabase
    }

    override fun onCreate(db: SQLiteDatabase) {
        println("Creating database and state table...")

        try {
            db.execSQL(
                """
                    CREATE TABLE IF NOT EXISTS $TABLE_STATE (
                $COLUMN_KEY TEXT PRIMARY KEY,
                $COLUMN_VALUE TEXT
            )
            """.trimIndent()
            )
            println("Table $TABLE_STATE created successfully")

            db.execSQL(
                """
                CREATE TABLE IF NOT EXISTS $TABLE_TOTP (
                    $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COLUMN_ACCOUNT_NAME TEXT NOT NULL,
                    $COLUMN_SECRET TEXT NOT NULL,
                    $COLUMN_ISSUER TEXT,
                    $COLUMN_ALGORITHM TEXT DEFAULT 'SHA1',
                    $COLUMN_DIGITS INTEGER DEFAULT 6,
                    $COLUMN_PERIOD INTEGER DEFAULT 30,
                    $COLUMN_LOGO_URL TEXT,
                    $COLUMN_NEW_OTP TEXT,
                    $COLUMN_REMAINING_TIME INTEGER,
                    $COLUMN_CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """.trimIndent()
            )
            println("Table $TABLE_TOTP created successfully")

        } catch (e: Exception) {
            println("Error creating table: ${e.message}")
        }
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STATE") // Delete old data
        onCreate(db) // Recreate the table
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STATE")
        onCreate(db)
    }

    fun saveState(key: String, value: String) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_KEY, key)
            put(COLUMN_VALUE, value)
        }
        db.insertWithOnConflict(TABLE_STATE, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun getState(key: String): String? {
        try {
            val db = readableDatabase
            val cursor: Cursor = db.query(
                TABLE_STATE,
                arrayOf(COLUMN_VALUE),
                "$COLUMN_KEY = ?",
                arrayOf(key),
                null,
                null,
                null
            )
            return if (cursor.moveToFirst()) {
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE))
            } else {
                null
            }.also {
                cursor.close()
            }
        } catch (e: SQLiteException) {
            println(e)
            return ""
        }
    }

    fun getPin(): String? {
        return this.getState("pin")
    }

    fun isPinEnabled(): Boolean {
        return this.getState("pin_enabled") == "1"
    }

    fun savePin(pin: String) {
        this.saveState("pin", pin)
        this.saveState("pin_enabled", "1")
    }

    fun setPinEnabled(isEnabled: Boolean) {
        this.saveState("pin_enabled", if (isEnabled) "1" else "0")
    }

    fun deleteState(key: String) {
        val db = writableDatabase
        db.delete(TABLE_STATE, "$COLUMN_KEY = ?", arrayOf(key))
    }


    fun saveAuth(auth: Auth) {
        val authStateKey = "auth"
        val db = writableDatabase
        val gson = Gson()
        val authJson = gson.toJson(auth)

        val contentValues = ContentValues().apply {
            put(COLUMN_KEY, authStateKey)
            put(COLUMN_VALUE, authJson)
        }
        db.insertWithOnConflict(TABLE_STATE, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun getAuth(): Auth? {
        val authJson = getState("auth") ?: return null
        return Gson().fromJson(authJson, Auth::class.java)
    }

    fun clearAuth() {
        val db = writableDatabase
        db.delete(TABLE_STATE, "$COLUMN_KEY = ?", arrayOf("auth"))
    }


    fun insertTotpEntry(totp: AuthenticatorEntry, newOtp: String, remainingTime: Float): String {
        try {
            val db = writableDatabase
            val contentValues = ContentValues().apply {
                put(COLUMN_ACCOUNT_NAME, totp.accountName)
                put(COLUMN_SECRET, totp.secret)
                put(COLUMN_ISSUER, totp.issuer)
                put(COLUMN_ALGORITHM, totp.algorithm)
                put(COLUMN_DIGITS, totp.digits)
                put(COLUMN_PERIOD, totp.period)
                put(COLUMN_LOGO_URL, totp.logoUrl)
                put(COLUMN_NEW_OTP, newOtp)
                put(COLUMN_REMAINING_TIME, remainingTime)
                put(COLUMN_CREATED_AT, System.currentTimeMillis())
            }
            val rowId = db.insert(TABLE_TOTP, null, contentValues)
            if (rowId == -1L) {
                Log.e("Database", "Error inserting row into ${TABLE_TOTP}")

            }
            return rowId.toString()
        } catch (e: Exception) {
            Log.e("insertTotpEntry", e.toString())
            return ""
        }
    }

    fun updateTotpEntry(id: String, newOtp: String): Int {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NEW_OTP, newOtp)
        }
        return db.update(TABLE_TOTP, contentValues, "${COLUMN_ID} = ?", arrayOf(id))
    }

    fun getAllTotpEntries(): List<AuthenticatorEntry> {
        val db = readableDatabase
        val cursor: Cursor = db.query(TABLE_TOTP, null, null, null, null, null, null)
        val totpList = mutableListOf<AuthenticatorEntry>()

        if (cursor.moveToFirst()) {
            do {
                val accountName =
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACCOUNT_NAME))
                val secret = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        COLUMN_SECRET
                    )
                )
                val id = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        COLUMN_ID
                    )
                )
                val issuer = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        COLUMN_ISSUER
                    )
                )
                val algorithm = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        COLUMN_ALGORITHM
                    )
                )
                val digits = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                        COLUMN_DIGITS
                    )
                )
                val period = cursor.getInt(
                    cursor.getColumnIndexOrThrow(
                        COLUMN_PERIOD
                    )
                )
                val createdAt =
                    cursor.getLongOrNull(
                        cursor.getColumnIndexOrThrow(
                            COLUMN_CREATED_AT
                        )
                    )
                val logoUrl = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                        COLUMN_LOGO_URL
                    )
                )
                val newOtp =
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NEW_OTP)) // Retrieve the new OTP
                val remainingTime =
                    cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_REMAINING_TIME)) // Retrieve the remaining time

                totpList.add(
                    AuthenticatorEntry(
                        id,
                        "otpauth",
                        "totp",
                        accountName,
                        secret,
                        issuer,
                        algorithm,
                        digits,
                        period,
                        logoUrl,
                        newOtp,
                        remainingTime,
                        createdAt = createdAt ?: 0
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return totpList
    }

    fun findOneBySecret(secret: String): AuthenticatorEntry? {
        val db = readableDatabase
        val selection = "${COLUMN_SECRET} = ?"
        val selectionArgs = arrayOf(secret)

        val cursor: Cursor = db.query(TABLE_TOTP, null, selection, selectionArgs, null, null, null)

        var totpEntry: AuthenticatorEntry? = null
        if (cursor.moveToFirst()) {
            val accountName = cursor.getString(
                cursor.getColumnIndexOrThrow(
                    COLUMN_ACCOUNT_NAME
                )
            )
            val id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val issuer = cursor.getString(
                cursor.getColumnIndexOrThrow(
                    COLUMN_ISSUER
                )
            )
            val algorithm = cursor.getString(
                cursor.getColumnIndexOrThrow(
                    COLUMN_ALGORITHM
                )
            )
            val digits = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DIGITS))
            val period = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PERIOD))
            val createdAt =
                cursor.getLongOrNull(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)) ?: 0
            val logoUrl = cursor.getString(
                cursor.getColumnIndexOrThrow(
                    COLUMN_LOGO_URL
                )
            )
            val newOtp = cursor.getString(
                cursor.getColumnIndexOrThrow(
                    COLUMN_NEW_OTP
                )
            )
            val remainingTime = cursor.getFloat(
                cursor.getColumnIndexOrThrow(
                    COLUMN_REMAINING_TIME
                )
            )

            totpEntry = AuthenticatorEntry(
                id, "otpauth", "totp", accountName, secret, issuer,
                algorithm, digits, period, logoUrl, newOtp, remainingTime, createdAt
            )
        }
        cursor.close()
        return totpEntry
    }

    fun deleteTotpEntry(secret: String) {
        val db = writableDatabase
        val res = db.delete(TABLE_TOTP, "${COLUMN_SECRET} = ?", arrayOf(secret))
        Log.d("app connection delete result", res.toString())
    }
}
