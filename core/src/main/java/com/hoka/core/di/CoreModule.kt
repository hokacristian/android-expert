package com.hoka.core.di

import androidx.room.Room
import com.hoka.core.data.source.StoryRepository
import com.hoka.core.data.source.local.LocalDataSource
import com.hoka.core.data.source.local.room.StoryDatabase
import com.hoka.core.data.source.remote.RemoteDataSource
import com.hoka.core.data.source.remote.network.ApiService
import com.hoka.core.domain.repository.IStoryRepository
import com.hoka.core.utils.AppExecutors
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<StoryDatabase>().storyDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            StoryDatabase::class.java, "Story.db"
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get(), get()) }
    factory { AppExecutors() }
    single<IStoryRepository> {
        StoryRepository(
            get(),
            get(),
            get()
        )
    }
}