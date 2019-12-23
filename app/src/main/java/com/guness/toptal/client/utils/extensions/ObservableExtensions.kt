package com.guness.toptal.client.utils.extensions

import io.reactivex.Flowable

fun <T, R> Flowable<List<T>>.mapList(map: (T) -> R) = map { it.map(map) }