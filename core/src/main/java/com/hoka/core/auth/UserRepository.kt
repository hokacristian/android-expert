package com.hoka.core.auth

class UserRepository(private val sesi: SessionManager) {

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(sesi: SessionManager): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(sesi)
            }
    }

    fun getUser() = sesi.getFromPreference(SessionManager.KEY_USERNAME)

    fun getToken() = sesi.getFromPreference(SessionManager.KEY_TOKEN)

    fun isUserLogin() = sesi.isLogin

    fun logoutUser() = sesi.logout()
}