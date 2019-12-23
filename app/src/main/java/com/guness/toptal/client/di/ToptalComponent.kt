package com.guness.toptal.client.di

import com.guness.toptal.client.core.ActivityInjectTarget
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AppModule::class,
        RoomModule::class,
        GsonModule::class,
        NetworkModule::class,
        ViewModelModule::class
    ]
)
interface ToptalComponent : AppInjector

interface AppInjector {
    fun inject(target: ActivityInjectTarget)
}