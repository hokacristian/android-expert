package com.hoka.expertsubmission.detail

import androidx.lifecycle.ViewModel
import com.hoka.core.domain.model.Story
import com.hoka.core.domain.usecase.StoryUseCase

class DetailViewModel(private val storyUseCase: StoryUseCase) : ViewModel() {
    fun setFavoriteStory(story: Story, newStatus: Boolean) =
        storyUseCase.setFavoriteStory(story, newStatus)
}