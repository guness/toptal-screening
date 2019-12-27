package com.guness.toptal.client.core

import androidx.lifecycle.ViewModel
import com.guness.toptal.client.utils.errors.ToptalException
import com.guness.toptal.client.utils.rx.RxActivityIndicator
import com.guness.toptal.client.utils.rx.trackActivity
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    protected val disposables = CompositeDisposable()

    val errors = BehaviorSubject.create<ToptalException>()
    val activityIndicator = RxActivityIndicator()

    open fun onStart() {}

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
        activityIndicator.clear()
    }

    // -- RX.react() --
    protected fun <T> Single<T>.react() = track().catch()

    protected fun <T> Observable<T>.react() = track().catch()
    protected fun Completable.react() = track().catch()


    // -- RX.dontCare() --
    protected fun <T> Single<T>.ignoreResult() = track().onErrorResumeNext {
        Timber.d(it, "Ignored result")
        Single.never()
    }

    // -- RX.track() --
    protected fun <T> Maybe<T>.track() = trackActivity(activityIndicator)

    protected fun <T> Single<T>.track() = trackActivity(activityIndicator)
    protected fun <T> Observable<T>.track() = trackActivity(activityIndicator)
    protected fun Completable.track() = trackActivity(activityIndicator)

    // -- RX.subs() --
    protected fun <T> Single<T>.subs() = react().subscribe().addTo(disposables)

    protected fun <T> Observable<T>.subs() = react().subscribe().addTo(disposables)
    protected fun Completable.subs() = react().subscribe().addTo(disposables)

    // -- RX.catch() --
    protected fun <T> Single<T>.catch() = onErrorResumeNext {
        errors.onNext(ToptalException.from(it))
        Single.never()
    }

    protected fun <T> Observable<T>.catch() = onErrorResumeNext { err: Throwable ->
        errors.onNext(ToptalException.from(err))
        Observable.empty()
    }

    protected fun Completable.catch() = onErrorResumeNext {
        errors.onNext(ToptalException.from(it))
        Completable.never()
    }
}