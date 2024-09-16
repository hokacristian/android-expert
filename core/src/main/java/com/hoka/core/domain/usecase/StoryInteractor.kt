package com.hoka.core.domain.usecase

import com.hoka.core.domain.model.Story
import com.hoka.core.domain.repository.IStoryRepository

class StoryInteractor(private val storyRepository: IStoryRepository): StoryUseCase {

    override fun getAllStory() = storyRepository.getAllStory()

    override fun getFavoriteStory() = storyRepository.getFavoriteStory()

    override fun setFavoriteStory(story: Story, state: Boolean) = storyRepository.setFavoriteStory(story, state)

    override suspend fun login(email: String, password: String) = storyRepository.login(email, password)

}