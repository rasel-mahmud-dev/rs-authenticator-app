package com.rs.rsauthenticator.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import com.google.gson.Gson
import com.rs.rsauthenticator.state.Auth

class AppStateDbHelper private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ApplicationState.db"
        private const val DATABASE_VERSION = 2

        const val TABLE_STATE = "state"
        private const val COLUMN_KEY = "key"
        private const val COLUMN_VALUE = "value"

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
        val createTableQuery = """
        CREATE TABLE IF NOT EXISTS $TABLE_STATE (
            $COLUMN_KEY TEXT PRIMARY KEY,
            $COLUMN_VALUE TEXT
        )
    """.trimIndent()
        try {
            db.execSQL(createTableQuery)
            println("Table $TABLE_STATE created successfully")
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
            put(authStateKey, authJson)
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
}
