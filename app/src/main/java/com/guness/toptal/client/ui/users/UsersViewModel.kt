package com.guness.toptal.client.ui.users

import com.guness.toptal.client.core.BaseViewModel
import com.guness.toptal.client.data.repositories.UsersRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.rxkotlin.Flowables
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class UsersViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : BaseViewModel() {
    val filter = BehaviorSubject.createDefault<CharSequence>("")

    val users = Flowables.combineLatest(usersRepository.usersWithEntries(), filter.toFlowable(BackpressureStrategy.LATEST))
        .map { pair ->
            val query = pair.second.trim()
            val list = pair.first
            if (query.isEmpty()) {
                list
            } else {
                list.filter { it.user.name.contains(query, true) }
            }
        }
}