package com.guness.toptal.client.data.repositories

import com.guness.toptal.protocol.dto.User
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor() {

    private var profile = BehaviorSubject.create<User>()
    private val session = BehaviorSubject.createDefault(false)

    fun postProfile(user: User) {
        profile.onNext(user)
        session.onNext(true)
    }

    fun clearProfile() {
        profile.onComplete()
        profile = BehaviorSubject.create<User>()
        session.onNext(false)
    }

    fun observeProfile() = profile.hide()
    fun observeSession() = session.hide().distinctUntilChanged()
}