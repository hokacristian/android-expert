package com.hoka.expertsubmission.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hoka.core.domain.usecase.StoryUseCase

class FavoriteViewModel(storyUseCase: StoryUseCase): ViewModel() {
    val favoriteStory = storyUseCase.getFavoriteStory().asLiveData()
}