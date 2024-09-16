package com.hoka.core.data.source.remote.network

import com.hoka.core.data.source.remote.response.LoginRequest
import com.hoka.core.data.source.remote.response.LoginResponse
import com.hoka.core.data.source.remote.response.StoryResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String
    ): StoryResponse
}