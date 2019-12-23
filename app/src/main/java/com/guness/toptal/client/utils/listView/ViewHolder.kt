package com.guness.toptal.client.utils.listView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.Disposable

class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private var disposable: Disposable? = null

    fun bind(binder: ViewBinder) {
        binder.bind(view)
    }

    fun bind(binder: DisposableViewBinder) {
        disposable?.dispose()
        disposable = binder.bind(view)
    }

    fun clear() {
        disposable?.dispose()
    }
}