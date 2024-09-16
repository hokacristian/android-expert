package com.hoka.core.data.source.remote

import android.util.Log
import com.hoka.core.auth.UserRepository
import com.hoka.core.data.source.remote.network.ApiResponse
import com.hoka.core.data.source.remote.network.ApiService
import com.hoka.core.data.source.remote.response.ListStoryItem
import com.hoka.core.data.source.remote.response.LoginRequest
import com.hoka.core.data.source.remote.response.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService, private val userRepository: UserRepository) {
    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(LoginRequest(email, password))
    }

    suspend fun getAllStory(): Flow<ApiResponse<List<ListStoryItem>>> {
        return flow {
            try {
                val token = userRepository.getToken()
                val response = apiService.getStories("Bearer $token")
                val dataArray = response.listStory
                if (dataArray.isNotEmpty()){
                    emit(ApiResponse.Success(response.listStory))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e : Exception){
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}