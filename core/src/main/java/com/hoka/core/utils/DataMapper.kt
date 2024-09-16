package com.hoka.core.utils

import com.hoka.core.data.source.local.entity.StoryEntity
import com.hoka.core.data.source.remote.response.ListStoryItem
import com.hoka.core.domain.model.Story

object DataMapper {
    fun mapResponsesToEntities(input: List<ListStoryItem>): List<StoryEntity> {
        val storyList = ArrayList<StoryEntity>()
        input.map {
            val story = StoryEntity(
                id = it.id.toString(),
                description = it.description.toString(),
                name = it.name.toString(),
                photoUrl = it.photoUrl.toString(),
                createdAt = it.createdAt.toString(),
                isFavorite = false
            )
            storyList.add(story)
        }
        return storyList
    }

    fun mapEntitiesToDomain(input: List<StoryEntity>): List<Story> =
        input.map {
            Story(
                id = it.id,
                description = it.description,
                name = it.name,
                photoUrl = it.photoUrl,
                createdAt = it.createdAt,
                isFavorite = it.isFavorite
            )
        }

    fun mapDomainToEntity(input: Story) = StoryEntity(
        id = input.id.toString(),
        description = input.description.toString(),
        name = input.name.toString(),
        photoUrl = input.photoUrl.toString(),
        createdAt = input.createdAt.toString(),
        isFavorite = input.isFavorite
    )
}