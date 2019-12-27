package com.guness.toptal.client.ui.user

import com.guness.toptal.client.core.BaseViewModel
import com.guness.toptal.client.data.repositories.UsersRepository
import com.guness.toptal.client.utils.extensions.takeSingle
import com.guness.toptal.protocol.dto.UserRole
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import javax.inject.Inject

class CreateUserViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : BaseViewModel() {

    val role = BehaviorSubject.createDefault(Optional.empty<UserRole>())

    val users = usersRepository.usersWithEntries()

    fun createUser(name: String, username: String, password: String): Single<Unit> {
        return role.takeSingle().flatMap { usersRepository.createUser(name, username, password) }.react().map { }
    }
}