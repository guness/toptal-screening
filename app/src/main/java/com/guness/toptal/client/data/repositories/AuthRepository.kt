package com.guness.toptal.client.data.repositories

import androidx.annotation.AnyThread
import com.guness.toptal.client.data.WebService
import com.guness.toptal.protocol.request.LoginRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val webService: WebService,
    private val profileRepository: ProfileRepository
) {

    @AnyThread
    fun login(username: String, password: String) = webService.login(LoginRequest(username, password))
        .doOnSuccess {
            profileRepository.bearerToken = it.token
            profileRepository.postProfile(it.user)
        }
        .ignoreElement()
}