package com.guness.toptal.client.utils.extensions

import com.google.gson.annotations.SerializedName
import timber.log.Timber

fun <E : Enum<*>> E.serializedName(): String? {
    try {
        return javaClass.getField(name).getAnnotation(SerializedName::class.java)?.value ?: name
    } catch (e: Exception) {
        Timber.e(e)
    }
    return null
}