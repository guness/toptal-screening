package com.guness.toptal.client.ui.profile

import com.guness.toptal.client.core.BaseViewModel
import com.guness.toptal.client.data.repositories.EntryRepository
import com.guness.toptal.client.data.repositories.ProfileRepository
import com.guness.toptal.client.data.repositories.UsersRepository
import com.guness.toptal.client.utils.extensions.takeSingle
import com.guness.toptal.protocol.dto.User
import com.guness.toptal.protocol.dto.UserRole
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val entryRepository: EntryRepository,
    private val usersRepository: UsersRepository,
    private val profileRepository: ProfileRepository
) : BaseViewModel() {

    val user = BehaviorSubject.create<User>()
    val role = BehaviorSubject.create<UserRole>()
    val self = profileRepository.observeProfile().map { it.get() }
    val entries = user.flatMap { entryRepository.entriesByUser(it.id).toObservable() }
    val admin = profileRepository.observeAdmin().distinctUntilChanged()
    val canEditName = Observables.combineLatest(admin, user, self).map {
        it.first || it.second.id == it.third.id
    }

    fun initSelf(): Observable<Unit> {
        return self
            .doOnNext(user::onNext)
            .map { }
    }

    fun save(newName: String): Single<Unit> {
        return Observables.combineLatest(user, self, role)
            .takeSingle()
            .flatMap { triple ->
                val selfChange = triple.first.id == triple.second.id

                val role = triple.third.takeUnless { selfChange }
                val name = newName.takeUnless { it == triple.first.name }

                if (selfChange) {
                    usersRepository.updateSelf(name!!)
                } else {
                    usersRepository.updateUser(triple.first.id, name, role)
                }
            }
            .react()
            .map { }
    }
}
