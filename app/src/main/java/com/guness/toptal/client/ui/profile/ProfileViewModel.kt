package com.guness.toptal.client.ui.profile

import com.guness.toptal.client.core.BaseViewModel
import com.guness.toptal.client.data.repositories.EntryRepository
import com.guness.toptal.client.data.repositories.ProfileModel
import com.guness.toptal.client.data.repositories.UsersRepository
import com.guness.toptal.client.utils.extensions.takeSingle
import com.guness.toptal.protocol.dto.User
import com.guness.toptal.protocol.dto.UserRole
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val entryRepository: EntryRepository,
    private val usersRepository: UsersRepository,
    private val profileModel: ProfileModel
) : BaseViewModel() {

    val user = BehaviorSubject.create<User>()
    val role = BehaviorSubject.create<UserRole>() //TODO: make optional
    val self = profileModel.observeProfile().map { it.get() }
    val selfProfile = Observables.combineLatest(user, self).map { it.first.id == it.second.id }
    val entries = user.flatMap { entryRepository.entriesByUser(it.id).toObservable() }
    val admin = profileModel.observeAdmin().distinctUntilChanged()

    fun initSelf(): Observable<Unit> {
        return self
            .doOnNext(user::onNext)
            .map { }
    }

    fun save(newName: String): Single<Unit> {
        return Observables.combineLatest(user, selfProfile, role)
            .takeSingle()
            .flatMap { triple ->

                val role = triple.third.takeUnless { triple.second }
                val name = newName.takeUnless { it == triple.first.name }

                if (triple.second) {
                    usersRepository.updateUser(name!!)
                } else {
                    usersRepository.updateUser(triple.first.id, name, role)
                }
            }
            .react()
            .map { }
    }

    fun changePassword(password: String): Single<Unit> {
        return Observables.combineLatest(user, selfProfile)
            .takeSingle()
            .flatMap {
                if (it.second) {
                    usersRepository.changePassword(password)
                } else {
                    usersRepository.changePassword(it.first.id, password)
                }
            }
            .react()
            .map { }
    }

    fun deleteUser(): Completable {
        return user.take(1).flatMapCompletable(usersRepository::deleteUser).react()
    }
}
