package com.guness.toptal.client.data.repositories

import androidx.annotation.AnyThread
import com.guness.toptal.client.data.WebService
import com.guness.toptal.client.data.storage.AuthStore
import com.guness.toptal.protocol.request.LoginRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val webService: WebService,
    private val profileRepository: ProfileRepository,
    private val authStore: AuthStore
) {

    @AnyThread
    fun login(username: String, password: String) = webService.login(LoginRequest(username, password))
        .doOnSuccess {
            authStore.bearerToken = it.token
            profileRepository.postProfile(it.user)
        }
        .ignoreElement()
}