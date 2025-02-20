package com.rs.rsauthenticator.database


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import androidx.core.database.getLongOrNull
import com.rs.rsauthenticator.database.AppStateDbHelper.Companion.TABLE_STATE
import com.rs.rsauthenticator.dto.TotpUriData


class TotpDatabaseHelper private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "TotpDatabase.db"
        private const val DATABASE_VERSION = 3 // Incremented version number

        private const val TABLE_TOTP = "totp_entries"
        private const val COLUMN_ID = "id"
        private const val COLUMN_ACCOUNT_NAME = "account_name"
        private const val COLUMN_SECRET = "secret"
        private const val COLUMN_ISSUER = "issuer"
        private const val COLUMN_ALGORITHM = "algorithm"
        private const val COLUMN_DIGITS = "digits"
        private const val COLUMN_PERIOD = "period"
        private const val COLUMN_LOGO_URL = "logo_url"
        private const val COLUMN_NEW_OTP = "new_otp" // New column for OTP
        private const val COLUMN_REMAINING_TIME = "remaining_time" // New column for remaining time
        private const val COLUMN_CREATED_AT = "created_at" // New column for remaining time

        @Volatile
        private var instance: TotpDatabaseHelper? = null

        fun getInstance(context: Context): TotpDatabaseHelper {
            return instance ?: synchronized(this) {
                instance ?: TotpDatabaseHelper(context.applicationContext).also { instance = it }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_TOTP (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ACCOUNT_NAME TEXT NOT NULL,
                $COLUMN_SECRET TEXT NOT NULL,
                $COLUMN_ISSUER TEXT NOT NULL,
                $COLUMN_ALGORITHM TEXT NOT NULL,
                $COLUMN_DIGITS INTEGER NOT NULL,
                $COLUMN_PERIOD INTEGER NOT NULL,
                $COLUMN_LOGO_URL TEXT,
                $COLUMN_NEW_OTP TEXT,  -- New OTP column
                $COLUMN_REMAINING_TIME REAL,  -- Remaining time column
                $COLUMN_CREATED_AT INTEGER 
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // Add the new columns one at a time for version 2
            val alterTableQuery1 = """
        ALTER TABLE $TABLE_TOTP
        ADD COLUMN $COLUMN_NEW_OTP TEXT
        """.trimIndent()
            db.execSQL(alterTableQuery1)

            val alterTableQuery2 = """
        ALTER TABLE $TABLE_TOTP
        ADD COLUMN $COLUMN_REMAINING_TIME REAL
        """.trimIndent()
            db.execSQL(alterTableQuery2)
        }

        if (oldVersion < 3) {
            // Add the new created_at column for version 3
            val alterTableQuery3 = """
        ALTER TABLE $TABLE_TOTP
        ADD COLUMN $COLUMN_CREATED_AT INTEGER
        """.trimIndent()
            db.execSQL(alterTableQuery3)
        }
    }


    fun insertTotpEntry(totp: TotpUriData, newOtp: String, remainingTime: Float): String {
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
                Log.e("Database", "Error inserting row into $TABLE_TOTP")
            }
            return rowId.toString()
        } catch (e: Exception) {
            println("sdffffffffffffffffffff:::::::::::::::SDFSDFSDFSD:SD:F:SDF:S")
            println(e)
            return ""
        }
    }

    fun updateTotpEntry(id: String, newOtp: String): Int {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NEW_OTP, newOtp) // Update the OTP
        }
        // Update the row with the specific ID
        return db.update(TABLE_TOTP, contentValues, "$COLUMN_ID = ?", arrayOf(id))
    }


    fun updateOtpRemainingTime(id: String, remainingTime: Long): Int {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_REMAINING_TIME, remainingTime)
        }
        return db.update(TABLE_TOTP, contentValues, "$COLUMN_ID = ?", arrayOf(id))
    }

    fun getAllTotpEntries(): List<TotpUriData> {
        val db = readableDatabase
        val cursor: Cursor = db.query(TABLE_TOTP, null, null, null, null, null, null)
        val totpList = mutableListOf<TotpUriData>()

        if (cursor.moveToFirst()) {
            do {
                val accountName =
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACCOUNT_NAME))
                val secret = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SECRET))
                val id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val issuer = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ISSUER))
                val algorithm = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALGORITHM))
                val digits = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DIGITS))
                val period = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PERIOD))
                val createdAt =
                    cursor.getLongOrNull(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT))
                val logoUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOGO_URL))
                val newOtp =
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NEW_OTP)) // Retrieve the new OTP
                val remainingTime =
                    cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_REMAINING_TIME)) // Retrieve the remaining time

                totpList.add(
                    TotpUriData(
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

    fun findOneBySecret(secret: String): TotpUriData? {
        val db = readableDatabase
        val selection = "$COLUMN_SECRET = ?"
        val selectionArgs = arrayOf(secret)

        val cursor: Cursor = db.query(TABLE_TOTP, null, selection, selectionArgs, null, null, null)

        var totpEntry: TotpUriData? = null
        if (cursor.moveToFirst()) {
            val accountName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ACCOUNT_NAME))
            val id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val issuer = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ISSUER))
            val algorithm = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALGORITHM))
            val digits = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DIGITS))
            val period = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PERIOD))
            val createdAt =
                cursor.getLongOrNull(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)) ?: 0
            val logoUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOGO_URL))
            val newOtp = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NEW_OTP))
            val remainingTime = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_REMAINING_TIME))

            totpEntry = TotpUriData(
                id, "otpauth", "totp", accountName, secret, issuer,
                algorithm, digits, period, logoUrl, newOtp, remainingTime, createdAt
            )
        }
        cursor.close()
        return totpEntry
    }


    fun deleteTotpEntry(id: String) {
        val db = writableDatabase
        val res = db.delete(TABLE_TOTP, "$COLUMN_ID = ?", arrayOf(id))
        println(res)
    }
}
