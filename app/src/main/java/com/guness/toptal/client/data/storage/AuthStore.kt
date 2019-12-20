package com.guness.toptal.client.data.storage

import com.guness.toptal.client.utils.AppContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthStore @Inject constructor(val context: AppContext) {
    var bearerToken: String? = null
    //TODO: store this on shared prefs or somewhere secure
}