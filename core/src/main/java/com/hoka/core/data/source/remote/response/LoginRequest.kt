package com.hoka.core.data.source.remote.response

data class LoginRequest(
    val email: String,
    val password: String
)