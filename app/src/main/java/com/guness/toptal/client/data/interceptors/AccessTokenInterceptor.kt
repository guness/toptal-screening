package com.guness.toptal.client.data.interceptors

import com.guness.toptal.client.data.repositories.ProfileRepository
import okhttp3.Interceptor
import okhttp3.Response

class AccessTokenInterceptor(private val profileRepository: ProfileRepository) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        profileRepository.bearerToken?.let {
            builder.header("Authorization", "Bearer $it")
        }
        return chain.proceed(builder.build())
    }
}