package com.guness.toptal.client.utils.rx

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject

class RxActivityIndicator : Observable<Boolean>() {

    private val subject: BehaviorSubject<Int> = BehaviorSubject.createDefault(0)

    private val loading = subject
        .observeOn(AndroidSchedulers.mainThread())
        .onErrorReturnItem(0).map { it > 0 }
        .distinctUntilChanged()

    val isLoading get() = subject.value!! > 0

    fun increment() {
        subject.onNext(subject.value!! + 1)
    }

    fun decrement() {
        subject.onNext(maxOf(subject.value!! - 1, 0))
    }

    fun clear() {
        subject.onNext(0)
    }

    override fun subscribeActual(observer: Observer<in Boolean>?) {
        if (observer == null) {
            return
        }
        loading.subscribe(observer)
    }

    internal fun <T> trackMaybe(source: Maybe<T>): Maybe<T> {
        return source
            .doOnSubscribe { increment() }
            .doFinally { decrement() }
    }

    internal fun <T> trackSingle(source: Single<T>): Single<T> {
        return source
            .doOnSubscribe { increment() }
            .doFinally { decrement() }
    }

    internal fun <T> trackObservable(source: Observable<T>): Observable<T> {
        return source
            .doOnSubscribe { increment() }
            .doFinally { decrement() }
    }

    internal fun trackCompletable(source: Completable): Completable {
        return source
            .doOnSubscribe { increment() }
            .doFinally { decrement() }
    }
}

fun <T> Maybe<T>.trackActivity(activityIndicator: RxActivityIndicator): Maybe<T> = activityIndicator.trackMaybe(this)

fun <T> Single<T>.trackActivity(activityIndicator: RxActivityIndicator): Single<T> = activityIndicator.trackSingle(this)

fun <T> Observable<T>.trackActivity(activityIndicator: RxActivityIndicator): Observable<T> = activityIndicator.trackObservable(this)

fun Completable.trackActivity(activityIndicator: RxActivityIndicator): Completable = activityIndicator.trackCompletable(this)