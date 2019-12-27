package com.guness.toptal.client.data.repositories

import androidx.annotation.AnyThread
import com.guness.toptal.client.data.WebService
import com.guness.toptal.protocol.request.CreateUserRequest
import com.guness.toptal.protocol.request.LoginRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val webService: WebService,
    private val profileModel: ProfileModel,
    private val entryRepository: EntryRepository,
    private val usersRepository: UsersRepository
) {

    @AnyThread
    fun login(username: String, password: String) = webService.login(LoginRequest(username, password))
        .doOnSuccess {
            profileModel.bearerToken = it.token
            usersRepository.postUser(it.user)
            profileModel.postProfile(it.user)
        }
        .ignoreElement()


    @AnyThread
    fun register(name: String, username: String, password: String) = webService.register(CreateUserRequest(name, username, password))
        .doOnSuccess {
            profileModel.bearerToken = it.token
            usersRepository.postUser(it.user)
            profileModel.postProfile(it.user)
        }
        .ignoreElement()

    @AnyThread
    fun logout() = webService.logout()
        .doOnComplete {
            entryRepository.clear()
            profileModel.clear()
            usersRepository.clear()
        }
}