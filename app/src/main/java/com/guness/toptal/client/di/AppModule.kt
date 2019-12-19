package com.guness.toptal.client.di

import com.guness.toptal.client.core.ToptalApplication
import com.guness.toptal.client.utils.AppContext
import dagger.Module
import dagger.Provides

@Module
class AppModule(val app: ToptalApplication) {

    @Provides
    fun provideApp(): ToptalApplication = app

    @Provides
    fun provideContext(): AppContext = app

}