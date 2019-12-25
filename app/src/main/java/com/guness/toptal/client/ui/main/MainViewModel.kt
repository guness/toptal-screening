package com.guness.toptal.client.ui.main

import com.guness.toptal.client.R
import com.guness.toptal.client.core.BaseViewModel
import com.guness.toptal.client.data.repositories.AuthRepository
import com.guness.toptal.client.data.repositories.EntryRepository
import com.guness.toptal.client.data.repositories.ProfileRepository
import com.guness.toptal.client.utils.extensions.mapList
import com.guness.toptal.client.utils.listView.DefaultAdapterModel
import com.guness.toptal.client.utils.listView.SingleTypeItemLayout
import io.reactivex.BackpressureStrategy
import io.reactivex.rxkotlin.Flowables
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val entryRepository: EntryRepository,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : BaseViewModel() {

    val filter = BehaviorSubject.createDefault<CharSequence>("")

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
        .mapList { SingleTypeItemLayout(R.layout.item_row_time_entry, TimeEntryItemViewModel(it)) }
        .map { DefaultAdapterModel(it.toTypedArray()) }

    override fun onStart() {
        disposables += profileRepository.observeSession()
            .filter { it }
            .flatMapMaybe {
                entryRepository.fetchEntries().react()
            }
            .subscribe()
    }

    fun login() {
        authRepository.login("user", "password").subs()
    }
}
