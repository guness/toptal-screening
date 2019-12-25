package com.guness.toptal.client.utils.extensions


import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.jakewharton.rxbinding3.widget.itemClickEvents
import io.reactivex.Observable


fun <T> AutoCompleteTextView.configureDropDownMenu(list: List<T>, toString: ((T) -> String)? = null): Observable<T> {
    val items = list.map { ToStringWrapper(it, toString) }
    val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_dropdown_item, items)
    setAdapter(adapter)

    return itemClickEvents().flatMap { event ->
        adapter.getItem(event.position)?.item?.let { Observable.just(it) } ?: Observable.empty()
    }
}

private class ToStringWrapper<T>(val item: T, val toString: ((T) -> String)? = null) {
    override fun toString(): String = toString?.invoke(item) ?: item.toString()
}