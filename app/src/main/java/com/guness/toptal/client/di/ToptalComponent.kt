package com.guness.toptal.client.di

import com.guness.toptal.client.core.ActivityInjectTarget
import com.guness.toptal.client.core.FragmentInjectTarget
import com.guness.toptal.client.core.TestInjectTarget
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
    fun inject(target: FragmentInjectTarget)
    fun inject(target: TestInjectTarget)
}