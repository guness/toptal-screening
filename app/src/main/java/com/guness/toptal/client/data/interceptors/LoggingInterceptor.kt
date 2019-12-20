package com.guness.toptal.client.data.interceptors

import com.guness.toptal.client.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

val loggingInterceptor = HttpLoggingInterceptor(
    object : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            largeLog("OkHttp", message)
        }
    })
    .also {
        it.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.HEADERS
        }
    }

private fun largeLog(tag: String, message: String) {
    val maxLen = 40000
    val batchLen = 4000
    if (message.length in (batchLen + 1) until maxLen) {
        Timber.d(message.substring(0, batchLen))
        largeLog(tag, message.substring(batchLen))
    } else {
        Timber.d(message)
    }
}