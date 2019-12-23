package com.guness.toptal.client.utils.extensions

import android.app.Activity
import android.content.Intent
import kotlin.reflect.KClass

inline fun <reified T> Activity.startActivity(clean: Boolean = false) {
    val intent = Intent(this, T::class.java)
    startActivity(intent, clean)
}

fun Activity.startActivity(intentClass: KClass<*>, clean: Boolean = false) {
    val intent = Intent(this, intentClass.java)
    startActivity(intent, clean)
}

fun Activity.startActivity(intent: Intent, clean: Boolean = false) {
    startActivity(intent)
    if (clean) {
        finishAffinity()
    }
}
