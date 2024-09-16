package com.hoka.expertsubmission.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hoka.core.domain.usecase.StoryUseCase

class HomeViewModel(storyUseCase: StoryUseCase) : ViewModel() {
    val story = storyUseCase.getAllStory().asLiveData()
}