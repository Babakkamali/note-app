package com.babakkamali.khatnevesht.utils

import android.content.Context
import androidx.sqlite.db.SimpleSQLiteQuery
import com.babakkamali.khatnevesht.data.database.NoteDatabase

object PreferenceUtils {

    private const val PREF_NAME = "app_prefs"
    private const val KEY_AUTH_TOKEN = "auth_token"
    val vacuumQuery = SimpleSQLiteQuery("VACUUM")

    fun saveToken(context: Context, token: String): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_AUTH_TOKEN, token)
        return editor.commit()
    }

    fun getToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null)
    }

    fun removeToken(context: Context): Boolean{
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(KEY_AUTH_TOKEN)
        clearDatabase(context)
        return editor.commit()
    }

    private fun clearDatabase(context: Context) {
        // Assuming you have a NoteDatabase and NoteDao for managing notes
        val noteDatabase = NoteDatabase.getInstance(context)
        val noteDao = noteDatabase.noteDao()
        noteDao.deleteAllNotes()
        noteDao.vacuumDatabase(vacuumQuery)
    }
}