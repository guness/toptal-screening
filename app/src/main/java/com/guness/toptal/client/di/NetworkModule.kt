package com.guness.toptal.client.di

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.guness.toptal.client.BuildConfig
import com.guness.toptal.client.data.WebService
import com.guness.toptal.client.data.interceptors.AccessTokenInterceptor
import com.guness.toptal.client.data.interceptors.UnauthorizedHandler
import com.guness.toptal.client.data.interceptors.loggingInterceptor
import com.guness.toptal.client.data.repositories.ProfileModel
import com.guness.toptal.client.di.scopes.ApiCall
import com.guness.toptal.client.utils.retrofit.EmptyBodyConverterFactory
import com.guness.toptal.client.utils.retrofit.EnumRetrofitConverterFactory
import com.guness.toptal.client.utils.rx.RxErrorHandlingCallAdapterFactory
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class NetworkModule {

    @Provides
    fun providesErrorHandlerCallAdapter(): RxErrorHandlingCallAdapterFactory {
        return RxErrorHandlingCallAdapterFactory(Schedulers.io())
    }

    @Provides
    @Singleton
    fun provideWebservice(@ApiCall retrofit: Retrofit): WebService = retrofit.create(WebService::class.java)

    @Provides
    @ApiCall
    fun providesApiInterceptors(profileModel: ProfileModel, unauthorizedHandler: UnauthorizedHandler): Array<Interceptor> {
        return arrayOf(
            unauthorizedHandler,
            AccessTokenInterceptor(profileModel),
            loggingInterceptor
        )
    }

    @Provides
    @Singleton
    @ApiCall
    fun providesApiCache(context: Context) = Cache(context.cacheDir, 10 * 1024 * 1024) // 10MB

    @Provides
    @Singleton
    @ApiCall
    fun providesApiRetrofit(
        @ApiCall okHttpClient: OkHttpClient, gson: Gson, errorAdapter: RxErrorHandlingCallAdapterFactory
    ): Retrofit {
        return buildRetrofit(okHttpClient, gson, errorAdapter)
    }

    @Provides
    @Singleton
    @ApiCall
    fun providesApiOkHttp(@ApiCall cache: Cache, @ApiCall interceptors: Array<Interceptor>): OkHttpClient {
        return okHttpBuilderWithInterceptors(interceptors)
            .cache(cache)
            .build()
    }

    private fun buildRetrofit(okHttpClient: OkHttpClient, gson: Gson, errorAdapter: RxErrorHandlingCallAdapterFactory): Retrofit {
        val builder = Retrofit.Builder()
        builder.baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(errorAdapter)
            .addConverterFactory(EmptyBodyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(EnumRetrofitConverterFactory())

        return builder.build()
    }

    private fun okHttpBuilderWithInterceptors(interceptors: Array<Interceptor>): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .followRedirects(true)

        interceptors.forEach { builder.addInterceptor(it) }

        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(StethoInterceptor())
        }

        return builder
    }
}