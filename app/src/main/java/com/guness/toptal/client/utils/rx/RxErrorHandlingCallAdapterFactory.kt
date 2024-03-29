package com.guness.toptal.client.utils.rx

import com.guness.toptal.client.utils.errors.ToptalException
import com.guness.toptal.protocol.dto.ErrorMessage
import io.reactivex.*
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.lang.reflect.Type

//CHECK HERE:  https://stackoverflow.com/a/56541415/1281930

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class RxErrorHandlingCallAdapterFactory(private val scheduler: Scheduler) : CallAdapter.Factory() {

    private var mOriginalCallAdapterFactory: RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

    @Suppress("UNCHECKED_CAST")
    override fun get(returnType: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit): CallAdapter<*, *>? {
        val rawType = getRawType(returnType)
        val wrapped = mOriginalCallAdapterFactory.get(returnType, annotations, retrofit)
        return when {
            rawType.isAssignableFrom(Maybe::class.java) -> RxCallAdapterWrapper.MaybeCallAdapter(
                retrofit,
                wrapped as CallAdapter<Any, Maybe<Any>>,
                scheduler
            )
            rawType.isAssignableFrom(Single::class.java) -> RxCallAdapterWrapper.SingleCallAdapter(
                retrofit,
                wrapped as CallAdapter<Any, Single<Any>>,
                scheduler
            )
            rawType.isAssignableFrom(Observable::class.java) -> RxCallAdapterWrapper.ObservableCallAdapter(
                retrofit,
                wrapped as CallAdapter<Any, Observable<Any>>,
                scheduler
            )
            rawType.isAssignableFrom(Completable::class.java) -> RxCallAdapterWrapper.CompletableCallAdapter(
                retrofit,
                wrapped as CallAdapter<Any, Completable>,
                scheduler
            )
            else -> null
        }
    }
}

sealed class RxCallAdapterWrapper<R, T>(
    val retrofit: Retrofit,
    val wrappedAdapter: CallAdapter<R, T>,
    val scheduler: Scheduler
) : CallAdapter<R, T> {
    override fun responseType(): Type {
        return wrappedAdapter.responseType()
    }

    class ObservableCallAdapter<R>(
        retrofit: Retrofit,
        wrappedAdapter: CallAdapter<R, Observable<R>>,
        scheduler: Scheduler
    ) : RxCallAdapterWrapper<R, Observable<R>>(retrofit, wrappedAdapter, scheduler) {
        override fun adapt(call: Call<R>): Observable<R> {
            return wrappedAdapter.adapt(call)
                .subscribeOn(scheduler)
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
                .onErrorResumeNext { err: Throwable -> Observable.error(asRetrofitException(retrofit, call.request().url.toString(), err)) }
        }
    }

    class SingleCallAdapter<R>(
        retrofit: Retrofit,
        wrappedAdapter: CallAdapter<R, Single<R>>,
        scheduler: Scheduler
    ) : RxCallAdapterWrapper<R, Single<R>>(retrofit, wrappedAdapter, scheduler) {
        override fun adapt(call: Call<R>): Single<R> {
            return wrappedAdapter.adapt(call)
                .subscribeOn(scheduler)
                .flatMap {
                    if (it is Response<*>) {
                        if (it.isSuccessful) {
                            Single.just<R>(it)
                        } else {
                            Single.error<R>(HttpException(it))
                        }
                    } else {
                        Single.just<R>(it)
                    }
                }
                .onErrorResumeNext { err: Throwable -> Single.error(asRetrofitException(retrofit, call.request().url.toString(), err)) }
        }
    }

    class MaybeCallAdapter<R>(
        retrofit: Retrofit,
        wrappedAdapter: CallAdapter<R, Maybe<R>>,
        scheduler: Scheduler
    ) : RxCallAdapterWrapper<R, Maybe<R>>(retrofit, wrappedAdapter, scheduler) {
        override fun adapt(call: Call<R>): Maybe<R> {
            return wrappedAdapter.adapt(call)
                .subscribeOn(scheduler)
                .flatMap {
                    if (it is Response<*>) {
                        if (it.isSuccessful) {
                            Maybe.just<R>(it)
                        } else {
                            Maybe.error<R>(HttpException(it))
                        }
                    } else {
                        Maybe.just<R>(it)
                    }
                }
                .onErrorResumeNext { err: Throwable -> Maybe.error(asRetrofitException(retrofit, call.request().url.toString(), err)) }
        }
    }

    class CompletableCallAdapter<R>(
        retrofit: Retrofit,
        wrappedAdapter: CallAdapter<R, Completable>,
        scheduler: Scheduler
    ) : RxCallAdapterWrapper<R, Completable>(retrofit, wrappedAdapter, scheduler) {
        override fun adapt(call: Call<R>): Completable {
            return wrappedAdapter.adapt(call)
                .subscribeOn(scheduler)
                .onErrorResumeNext { err: Throwable -> Completable.error(asRetrofitException(retrofit, call.request().url.toString(), err)) }
        }
    }
}

private fun asRetrofitException(retrofit: Retrofit?, url: String, throwable: Throwable): ToptalException {
    return when (throwable) {
        // We had non-200 http error
        is HttpException -> {
            val response = throwable.response()
            val converter = retrofit?.responseBodyConverter<ErrorMessage>(ErrorMessage::class.java, arrayOfNulls<Annotation>(0))
            val message: ErrorMessage? = try {
                response?.errorBody()?.let { converter?.convert(it) }
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
        else -> ToptalException.UnknownError(throwable)
    }
}
