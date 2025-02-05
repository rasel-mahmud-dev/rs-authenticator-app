package com.rs.rsauthenticator.database


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import com.rs.rsauthenticator.dto.TotpUriData


class TotpDatabaseHelper private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "TotpDatabase.db"
        private const val DATABASE_VERSION = 2 // Incremented version number

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
                $COLUMN_REMAINING_TIME REAL  -- Remaining time column
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // Add the new columns one at a time
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
    }


    fun insertTotpEntry(totp: TotpUriData, newOtp: String, remainingTime: Float): String {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_ACCOUNT_NAME, totp.accountName)
            put(COLUMN_SECRET, totp.secret)
            put(COLUMN_ISSUER, totp.issuer)
            put(COLUMN_ALGORITHM, totp.algorithm)
            put(COLUMN_DIGITS, totp.digits)
            put(COLUMN_PERIOD, totp.period)
            put(COLUMN_LOGO_URL, totp.logoUrl)
            put(COLUMN_NEW_OTP, newOtp) // Save the new OTP
            put(COLUMN_REMAINING_TIME, remainingTime) // Save the remaining time
        }
        val rowId = db.insert(TABLE_TOTP, null, contentValues)
        if (rowId == -1L) {
            Log.e("Database", "Error inserting row into $TABLE_TOTP")
        }
        return rowId.toString()
    }

    fun updateTotpEntry(id: String, newOtp: String, remainingTime: Float): Int {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NEW_OTP, newOtp) // Update the OTP
            put(COLUMN_REMAINING_TIME, remainingTime) // Update the remaining time
        }
        // Update the row with the specific ID
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
                        remainingTime
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return totpList
    }

    fun deleteTotpEntry(id: String) {
        val db = writableDatabase
        db.delete(TABLE_TOTP, "$COLUMN_ID = ?", arrayOf(id))
    }
}
