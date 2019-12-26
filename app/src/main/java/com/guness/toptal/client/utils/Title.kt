package com.guness.toptal.client.utils

import android.content.Context
import android.text.ParcelableSpan
import android.text.Spannable
import android.widget.TextView
import com.guness.toptal.client.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

sealed class Title {
    class Resource(val id: Int, vararg val params: Any) : Title()
    class Text(val value: String) : Title()
    class DynamicResource(val subject: BehaviorSubject<Int> = BehaviorSubject.createDefault(R.string.empty)) : Title()
    class DynamicText(val subject: BehaviorSubject<String> = BehaviorSubject.createDefault("")) : Title()
    class Spanned(val spannable: Spannable) : Title()

    val hashCode: Int
        get() = when (this) {
            is Resource -> params.fold(id, { acc, value -> acc * 31 + value.hashCode() })
            is Text -> value.hashCode()
            is DynamicResource -> subject.value ?: 0
            is DynamicText -> subject.value?.hashCode() ?: 0
            is Spanned -> {
                spannable.getSpans(0, spannable.length, ParcelableSpan::class.java).fold(spannable.toString().hashCode(), { acc, value ->
                    acc * 31 + value.spanTypeId
                })
            }
        }

    fun getText(context: Context): CharSequence? {
        return when (this) {
            is Text -> value
            is Resource -> if (params.isEmpty()) {
                context.getString(id)
            } else {
                context.getString(id, *params)
            }
            is DynamicText -> subject.value
            is DynamicResource -> subject.value?.let { context.getString(it) }
            is Spanned -> spannable
        }
    }
}

fun Title.bindTo(textView: TextView): Disposable {
    return when (this) {
        is Title.DynamicText -> subject
        is Title.DynamicResource -> subject.map { textView.context.getString(it) }
        else -> {
            Observable.fromCallable { getText(textView.context) }
        }
    }.observeOn(AndroidSchedulers.mainThread()).subscribe { textView.text = it }
}