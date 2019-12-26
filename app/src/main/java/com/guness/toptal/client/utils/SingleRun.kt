package com.guness.toptal.client.utils

import java.util.concurrent.atomic.AtomicBoolean

class SingleRun {

    private val fresh = AtomicBoolean(true)

    val isUsed: Boolean
        get() = !fresh.get()

    fun use(action: () -> Unit) {
        if (fresh.getAndSet(false)) {
            action()
        }
    }
}






