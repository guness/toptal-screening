package com.guness.toptal.client.utils.extensions

import io.reactivex.Flowable
import io.reactivex.Observable

fun <T, R> Flowable<List<T>>.mapList(map: (T) -> R) = map { it.map(map) }

fun <T> Observable<T>.takeSingle() = take(1).singleOrError()