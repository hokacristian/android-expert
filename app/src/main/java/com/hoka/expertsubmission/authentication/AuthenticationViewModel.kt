package com.hoka.expertsubmission.authentication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hoka.core.auth.SessionManager
import com.hoka.core.data.source.remote.response.LoginResponse
import com.hoka.core.domain.usecase.StoryUseCase
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val storyUseCase: StoryUseCase,
    private val sesi: SessionManager
) : ViewModel() {
    private val _loginResult = MutableLiveData<LoginResponse?>()
    val loginResult: LiveData<LoginResponse?> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = storyUseCase.login(email, password)
                sesi.createLoginSession(result.loginResult?.token.toString(), result.loginResult?.name.toString())
                _loginResult.value = result
                _isLoading.value = false
            } catch (e: Exception) {
                _loginResult.value = null
                Log.e(TAG, "login: ${e.message}" )
                _isLoading.value = false
            }
        }
    }

    companion object {
        private const val TAG = "AuthenticationViewModel"
    }
}