package com.rs.rsauthenticator.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import com.google.gson.Gson
import com.rs.rsauthenticator.state.Auth

class AuthDatabase private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "AuthState.db"
        private const val DATABASE_VERSION = 2

        private const val TABLE_AUTH = "auth"
        private const val COLUMN_KEY = "key"
        private const val COLUMN_VALUE = "value"

        private const val AUTH_KEY = "user_auth"

        @Volatile
        private var instance: AuthDatabase? = null

        fun getInstance(context: Context): AuthDatabase {
            return instance ?: synchronized(this) {
                instance ?: AuthDatabase(context.applicationContext).also { instance = it }
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_AUTH (
                $COLUMN_KEY TEXT PRIMARY KEY,
                $COLUMN_VALUE TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_AUTH")
        onCreate(db)
    }

    fun saveAuth(auth: Auth) {
        val db = writableDatabase
        val gson = Gson()
        val authJson = gson.toJson(auth)

        val contentValues = ContentValues().apply {
            put(COLUMN_KEY, AUTH_KEY)
            put(COLUMN_VALUE, authJson)
        }
        db.insertWithOnConflict(TABLE_AUTH, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun getAuth(): Auth? {
        val db = readableDatabase
        val cursor: Cursor = db.query(
            TABLE_AUTH,
            arrayOf(COLUMN_VALUE),
            "$COLUMN_KEY = ?",
            arrayOf(AUTH_KEY),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val json = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE))
            Gson().fromJson(json, Auth::class.java)
        } else {
            null
        }.also {
            cursor.close()
        }
    }

    fun clearAuth() {
        val db = writableDatabase
        db.delete(TABLE_AUTH, "$COLUMN_KEY = ?", arrayOf(AUTH_KEY))
    }
}
