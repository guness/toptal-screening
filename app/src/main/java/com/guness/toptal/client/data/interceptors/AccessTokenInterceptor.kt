package com.guness.toptal.client.data.interceptors

import com.guness.toptal.client.data.repositories.ProfileModel
import okhttp3.Interceptor
import okhttp3.Response

class AccessTokenInterceptor(private val profileModel: ProfileModel) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        profileModel.bearerToken?.let {
            builder.header("Authorization", "Bearer $it")
        }
        return chain.proceed(builder.build())
    }
}