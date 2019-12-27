package com.guness.toptal.client.ui.main

import com.guness.toptal.client.core.BaseViewModel
import com.guness.toptal.client.data.repositories.AuthRepository
import com.guness.toptal.client.data.repositories.EntryRepository
import com.guness.toptal.client.data.repositories.ProfileRepository
import com.guness.toptal.client.data.repositories.UsersRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.rxkotlin.Flowables
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val entryRepository: EntryRepository,
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository,
    private val profileRepository: ProfileRepository
) : BaseViewModel() {

    val filter = BehaviorSubject.createDefault<CharSequence>("")
    val manager = profileRepository.observeManager().toFlowable(BackpressureStrategy.LATEST)

    val users = usersRepository.users()

    val entries = Flowables.combineLatest(entryRepository.entries(), filter.toFlowable(BackpressureStrategy.LATEST))
        .map { pair ->
            val query = pair.second.trim()
            val list = pair.first
            if (query.isEmpty()) {
                list
            } else {
                list.filter { it.name.contains(query, true) || it.city.contains(query, true) }
            }
        }

    fun fetchUsers() {
        disposables += profileRepository.observeManager()
            .distinctUntilChanged()
            .filter { it }
            .flatMapSingle {
                usersRepository.fetchUsers().ignoreResult()
            }
            .subscribe()
    }

    fun fetchEntries() {
        disposables += profileRepository.observeSession()
            .filter { it }
            .flatMapSingle {
                entryRepository.fetchEntries().ignoreResult()
            }
            .subscribe()
    }

    fun logout() {
        authRepository.logout().subs()
    }
}
