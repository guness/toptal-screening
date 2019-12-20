package com.guness.toptal.client.data.interceptors

import com.guness.toptal.client.data.storage.AuthStore
import okhttp3.Interceptor
import okhttp3.Response

class AccessTokenInterceptor(private val authStore: AuthStore) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        authStore.bearerToken?.let {
            builder.header("Authorization", "Bearer $it")
        }
        return chain.proceed(builder.build())
    }
}