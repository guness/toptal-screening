package com.guness.toptal.client.data.repositories

import androidx.annotation.AnyThread
import androidx.annotation.WorkerThread
import com.guness.toptal.client.data.WebService
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
    private val profileRepository: ProfileRepository
) {

    fun users() = userDao.users()

    @WorkerThread
    fun postUser(user: User) = userDao.addUser(user)

    @WorkerThread
    fun removeUser(user: User) = userDao.deleteUser(user)

    @AnyThread
    fun fetchUsers() = webService.getUsers()
        .doOnSuccess { userDao.setUsers(it.users) }

    @AnyThread
    fun deleteUser(user: User) = webService.deleteUser(user.id)
        .doOnComplete { removeUser(user) }

    @AnyThread
    fun createUser(name: String, username: String, password: String) = webService.createUser(CreateUserRequest(name, username, password))
        .doOnSuccess(userDao::addUser)

    @AnyThread
    fun updateUser(id: Long, name: String? = null, role: UserRole? = null) = webService.updateUser(id, UpdateUserRequest(role = role, name = name))
        .doOnSuccess(userDao::addUser)

    @AnyThread
    fun updateUser(name: String) = webService.updateUser(UpdateUserRequest(name = name))
        .doOnSuccess(userDao::addUser)
        .doOnSuccess(profileRepository::postProfile)

    @AnyThread
    fun changePassword(id: Long, password: String) = webService.updateUser(id, UpdateUserRequest(password = password))
        .doOnSuccess(userDao::addUser)

    @AnyThread
    fun changePassword(password: String) = webService.updateUser(UpdateUserRequest(password = password))
        .doOnSuccess(profileRepository::postProfile)

    @WorkerThread
    fun clear() = userDao.clear()

    fun user(userId: Long) = userDao.user(userId)
}