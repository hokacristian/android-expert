package com.hoka.core.auth

class UserRepository(private val sesi: SessionManager) {

    fun getToken() = sesi.getFromPreference(SessionManager.KEY_TOKEN)

    fun isUserLogin() = sesi.isLogin

    fun logoutUser() = sesi.logout()
}