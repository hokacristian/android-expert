package com.hoka.core.domain.repository

import com.hoka.core.data.source.Resource
import com.hoka.core.data.source.remote.response.LoginResponse
import com.hoka.core.domain.model.Story
import kotlinx.coroutines.flow.Flow

interface IStoryRepository {
    fun getAllStory(): Flow<Resource<List<Story>>>
    fun getFavoriteStory(): Flow<List<Story>>
    fun setFavoriteStory(story: Story, state: Boolean)

    suspend fun login(email: String, password: String): LoginResponse
}