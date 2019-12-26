package com.guness.toptal.client.utils.extensions

import android.content.Intent
import java.io.Serializable

inline fun <reified T : Serializable> Intent.put(serializable: T): Intent {
    return putExtra(T::class.java.simpleName, serializable)
}

inline fun <reified T : Serializable> Intent.serializable(): T? {
    val key = T::class.java.simpleName
    return if (hasExtra(key) && getSerializableExtra(key) != null) getSerializableExtra(key) as T else null
}

inline fun <reified T : Serializable> Intent.hasSerializable(): Boolean {
    val key = T::class.java.simpleName
    return hasExtra(key) && getSerializableExtra(key) != null
}
