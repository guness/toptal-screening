package com.guness.toptal.client.utils.listView

import android.view.View
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.disposables.Disposable

interface ViewBinder {
    fun bind(view: View)
}

interface ViewBinderDelegate<T> {
    fun bind(view: View, viewModel: T)
}

interface DisposableViewBinder {
    @CheckReturnValue
    fun bind(view: View): Disposable
}

interface DisposableViewBinderDelegate<T> {
    @CheckReturnValue
    fun bind(view: View, viewModel: T): Disposable
}