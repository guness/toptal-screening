package com.guness.toptal.client.core

import android.app.Application
import com.facebook.stetho.Stetho
import com.guness.toptal.client.BuildConfig
import com.guness.toptal.client.di.AppInjector
import com.guness.toptal.client.di.AppModule
import com.guness.toptal.client.di.DaggerToptalComponent
import com.guness.toptal.client.di.ToptalComponent
import com.guness.toptal.client.utils.RxJava.setupErrorHooks
import timber.log.Timber

class ToptalApplication : Application() {

    val injector: ToptalComponent by lazy {
        DaggerToptalComponent.builder().appModule(AppModule(this)).build()
    }

    override fun onCreate() {
        super.onCreate()

        initLogger()
        initStetho()
        setupErrorHooks()
    }

    private fun initStetho() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    private fun initLogger() {
        Timber.plant(Timber.DebugTree())
    }
}

val Application.injector: AppInjector
    get() = (this as ToptalApplication).injector