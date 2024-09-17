package com.hoka.core.data.source

import com.hoka.core.data.source.local.LocalDataSource
import com.hoka.core.data.source.remote.RemoteDataSource
import com.hoka.core.data.source.remote.network.ApiResponse
import com.hoka.core.data.source.remote.response.ListStoryItem
import com.hoka.core.data.source.remote.response.LoginResponse
import com.hoka.core.domain.model.Story
import com.hoka.core.domain.repository.IStoryRepository
import com.hoka.core.utils.AppExecutors
import com.hoka.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoryRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
): IStoryRepository {
    override fun getAllStory(): Flow<Resource<List<Story>>> =
        object : NetworkBoundResource<List<Story>, List<ListStoryItem>>() {
            override fun loadFromDB(): Flow<List<Story>> {
                return localDataSource.getAllStory().map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Story>?): Boolean =
                data.isNullOrEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<ListStoryItem>>> =
                remoteDataSource.getAllStory()

            override suspend fun saveCallResult(data: List<ListStoryItem>) {
                val storyList = DataMapper.mapResponsesToEntities(data)
                localDataSource.insertStory(storyList)
            }
        }.asFlow()

    override fun getFavoriteStory(): Flow<List<Story>> {
        return localDataSource.getFavoriteStory().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override fun setFavoriteStory(story: Story, state: Boolean) {
        val storyEntity = DataMapper.mapDomainToEntity(story)
        appExecutors.diskIO().execute { localDataSource.setFavoriteTourism(storyEntity, state)}
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        return remoteDataSource.login(email, password)
    }
}