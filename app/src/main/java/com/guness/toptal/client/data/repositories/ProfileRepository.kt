package com.guness.toptal.client.data.repositories

import androidx.annotation.WorkerThread
import com.guness.toptal.protocol.dto.User
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(private val usersRepository: UsersRepository) {

    private var profile = BehaviorSubject.create<User>()
    private val session = BehaviorSubject.createDefault(false)

    @WorkerThread
    fun postProfile(user: User) {
        profile.onNext(user)
        session.onNext(true)
        usersRepository.postUser(user)
    }

    @WorkerThread
    fun clearProfile() {
        profile.value?.let(usersRepository::removeUser)
        profile.onComplete()
        profile = BehaviorSubject.create<User>()
        session.onNext(false)
    }

    fun observeProfile() = profile.hide()
    fun observeSession() = session.hide().distinctUntilChanged()
}