@file:Suppress("DEPRECATION")

package com.hoka.expertsubmission.di

import com.hoka.core.auth.SessionManager
import com.hoka.core.auth.UserRepository
import com.hoka.core.domain.usecase.StoryInteractor
import com.hoka.core.domain.usecase.StoryUseCase
import com.hoka.expertsubmission.authentication.AuthenticationViewModel
import com.hoka.expertsubmission.detail.DetailViewModel
import com.hoka.expertsubmission.home.HomeViewModel
import com.hoka.expertsubmission.viewmodel.FavoriteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<StoryUseCase> { StoryInteractor(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { AuthenticationViewModel(get(), get()) }
    viewModel { FavoriteViewModel(get()) }
}

val storageModule = module {
    factory {
        SessionManager(get())
    }

    factory {
        UserRepository(get())
    }
}