package com.guness.toptal.client.data.handlers

import com.guness.toptal.client.data.storage.AuthStore
import javax.inject.Inject

class CleanSessionHandler @Inject constructor(private val authStore: AuthStore) {
    fun clean() {
        authStore.bearerToken = null
    }
}