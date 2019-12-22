package com.guness.toptal.client.data.repositories

import com.guness.toptal.client.data.WebService
import com.guness.toptal.client.data.room.EntryDao
import com.guness.toptal.client.data.room.UserDao
import com.guness.toptal.protocol.dto.User
import com.guness.toptal.protocol.dto.UserRole
import com.guness.toptal.protocol.request.CreateUserRequest
import com.guness.toptal.protocol.request.UpdateUserRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(
    private val webService: WebService,
    private val userDao: UserDao,
    private val entryDao: EntryDao
) {

    fun users() = userDao.users()

    fun deleteUser(user: User) = webService.deleteUser(user.id)
        .doOnComplete { userDao.deleteUser(user) }

    fun createUser(username: String, password: String) = webService.createUser(CreateUserRequest(username, password))
        .doOnSuccess(userDao::addUser)

    fun roleUser(id: Long, role: UserRole) = webService.updateUser(id, UpdateUserRequest(role = role))
        .doOnSuccess(userDao::addUser)

    fun changePassword(id: Long, password: String) = webService.updateUser(id, UpdateUserRequest(password = password))
        .doOnSuccess(userDao::addUser)
}