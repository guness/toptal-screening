package com.guness.toptal.client.utils.rx

import com.guness.toptal.client.utils.exceptions.ToptalException
import com.guness.toptal.protocol.dto.ErrorMessage
import io.reactivex.Observable
import io.reactivex.Scheduler
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.lang.reflect.Type


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
/**
 * Call Adapter Factory with Medici's API error handling
 */
class RxErrorHandlingCallAdapterFactory(
    val subscribeScheduler: Scheduler,
    val observerScheduler: Scheduler
) : CallAdapter.Factory() {

    private var mOriginalCallAdapterFactory: RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

    override fun get(returnType: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): CallAdapter<*, *>? {
        return RxCallAdapterWrapper(retrofit, mOriginalCallAdapterFactory.get(returnType, annotations, retrofit) as CallAdapter<*, *>)
    }

    inner class RxCallAdapterWrapper<R>(private val mRetrofit: Retrofit?, private val mWrappedCallAdapter: CallAdapter<R, *>) : CallAdapter<R, Observable<R>> {

        override fun responseType(): Type {
            return mWrappedCallAdapter.responseType()
        }

        override fun adapt(call: Call<R>): Observable<R> {
            return (mWrappedCallAdapter.adapt(call) as Observable<R>)
                .subscribeOn(subscribeScheduler)
                .observeOn(observerScheduler)
                .flatMap {
                    if (it is Response<*>) {
                        if (it.isSuccessful) {
                            Observable.just<R>(it)
                        } else {
                            Observable.error<R>(HttpException(it))
                        }
                    } else {
                        Observable.just<R>(it)
                    }
                }
                .onErrorResumeNext { t: Throwable -> Observable.error(asRetrofitException(call.request().url.toString(), t)) }
        }

        /**
         * Parameter [url] is used only for logging
         * In case of unhandled exception it will be easier to find the cause
         * (MEDICI-2206, MEDICI-2209)
         */
        private fun asRetrofitException(url: String, throwable: Throwable): ToptalException {
            return when (throwable) {
                // We had non-200 http error
                is HttpException -> {
                    val response = throwable.response()
                    val converter = mRetrofit?.responseBodyConverter<ErrorMessage>(ErrorMessage::class.java, arrayOfNulls<Annotation>(0))
                    val message: ErrorMessage? = try {
                        converter?.convert(response?.errorBody())
                    } catch (e: Exception) {
                        null
                    }
                    if (message != null) {
                        // non-200 with server message (bad request or validation failed)
                        ToptalException.ApiError(url, throwable.code(), message, throwable)
                    } else {
                        // other kind of HTTP error (4xx, 5xx, etc)
                        ToptalException.HttpError(url, throwable.code(), throwable)
                    }
                }

                // A network error happened
                is IOException -> ToptalException.NetworkError(url, throwable)

                // Unexpected error
                else -> ToptalException.UnknownError(url, throwable)
            }
        }
    }
}