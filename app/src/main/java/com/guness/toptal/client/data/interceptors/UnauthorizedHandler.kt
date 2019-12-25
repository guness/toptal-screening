package com.guness.toptal.client.data.interceptors

import com.guness.toptal.client.data.handlers.CleanSessionHandler
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject

class UnauthorizedHandler @Inject constructor(private val cleanSessionHandler: CleanSessionHandler) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request()).also {
            if (it.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                cleanSessionHandler.clean()
            }
        }
    }
}