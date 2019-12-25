package com.guness.toptal.client.data.repositories

import androidx.annotation.WorkerThread
import com.guness.toptal.protocol.dto.User
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(private val usersRepository: UsersRepository) {

    private var profile = BehaviorSubject.createDefault<Optional<User>>(Optional.empty())
    private val session = BehaviorSubject.createDefault(false)

    @WorkerThread
    fun postProfile(user: User) {
        profile.onNext(Optional.of(user))
        session.onNext(true)
        usersRepository.postUser(user)
    }

    @WorkerThread
    fun clearProfile() {
        profile.value?.ifPresent(usersRepository::removeUser)
        profile.onNext(Optional.empty())
        session.onNext(false)
    }

    fun observeProfile() = profile.hide()
    fun observeSession() = session.hide().distinctUntilChanged()
}