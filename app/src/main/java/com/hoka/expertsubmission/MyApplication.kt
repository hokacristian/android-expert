package com.hoka.expertsubmission

import android.app.Application
import com.hoka.core.di.databaseModule
import com.hoka.core.di.networkModule
import com.hoka.core.di.repositoryModule
import com.hoka.expertsubmission.di.storageModule
import com.hoka.expertsubmission.di.useCaseModule
import com.hoka.expertsubmission.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level.NONE

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule,
                    storageModule
                    )
            )
        }
    }
}