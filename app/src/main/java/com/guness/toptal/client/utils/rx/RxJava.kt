package com.guness.toptal.client.utils.rx

import io.reactivex.exceptions.OnErrorNotImplementedException
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

object RxJava {

    /**
     * Handle only [UndeliverableException], let others crash the app
     * https://github.com/ReactiveX/RxJava/wiki/What%27s-different-in-2.0#error-handling
     */
    fun setupErrorHooks() {
        RxJavaPlugins.setErrorHandler { e ->
            if (e is UndeliverableException) {
                Timber.e(e.cause, "Undeliverable exception")
                return@setErrorHandler
            }
            if (e is OnErrorNotImplementedException) {
                Timber.e(e.cause, "OnErrorNotImplemented for the error")
            }
            Thread.currentThread().uncaughtExceptionHandler?.uncaughtException(Thread.currentThread(), e)
        }
    }
}
