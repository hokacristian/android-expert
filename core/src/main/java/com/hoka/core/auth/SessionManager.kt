package com.hoka.core.auth

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private var pref: SharedPreferences = context.getSharedPreferences("Session", Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = pref.edit()

    fun createLoginSession(token: String, username: String) {
        editor.putBoolean(KEY_LOGIN, true)
            .putString(KEY_TOKEN, token)
            .putString(KEY_USERNAME, username)
            .commit()
    }

    fun logout() {
        editor.clear()
        editor.commit()
    }

    val isLogin: Boolean
        get() = pref.getBoolean(KEY_LOGIN, false)

    fun saveToPreference(key: String, value: String) = editor.putString(key, value).commit()

    fun getFromPreference(key: String) = pref.getString(key, "")

    val token: String?
        get() = pref.getString(KEY_TOKEN, null)

    val username: String?
        get() = pref.getString(KEY_USERNAME, null)

    companion object {
        const val KEY_LOGIN = "isLogin"
        const val KEY_USERNAME = "username"
        const val KEY_TOKEN = "token"
    }

    fun isUserLogin(): Boolean {
        return !token.isNullOrEmpty()
    }
}
