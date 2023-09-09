package com.babakkamali.khatnevesht.utils

import android.content.Context

object PreferenceUtils {

    private const val PREF_NAME = "app_prefs"
    private const val KEY_AUTH_TOKEN = "auth_token"

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
        return editor.commit()
    }
}