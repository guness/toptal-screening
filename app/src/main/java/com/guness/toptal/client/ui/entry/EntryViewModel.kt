package com.guness.toptal.client.ui.entry

import com.guness.toptal.client.core.BaseViewModel
import com.guness.toptal.client.data.repositories.EntryRepository
import com.guness.toptal.client.data.repositories.ProfileModel
import com.guness.toptal.client.data.repositories.UsersRepository
import com.guness.toptal.client.utils.extensions.takeSingle
import com.guness.toptal.protocol.dto.TimeEntry
import com.guness.toptal.protocol.dto.User
import com.guness.toptal.protocol.dto.validTimeZoneIDs
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.joda.time.DateTimeZone
import java.util.*
import javax.inject.Inject

class EntryViewModel @Inject constructor(
    private val entryRepository: EntryRepository,
    private val usersRepository: UsersRepository,
    private val profileModel: ProfileModel
) : BaseViewModel() {

    val initialEntry = BehaviorSubject.createDefault<Optional<TimeEntry>>(Optional.empty())

    val timeZone = BehaviorSubject.createDefault<Optional<DateTimeZone>>(Optional.empty())

    val manager = profileModel.observeManager().filter { it }.distinctUntilChanged()

    val users = manager.toFlowable(BackpressureStrategy.LATEST).flatMap { usersRepository.users() }

    val user = BehaviorSubject.createDefault<Optional<User>>(Optional.empty())

    private val timeEntry = Observables.combineLatest(timeZone, initialEntry)
        .map { pair ->
            pair.first.map {
                pair.second.orElseGet { TimeEntry(timeZone = DateTimeZone.UTC) }.copy(timeZone = it)
            }
        }

    fun timeZoneIDs() = Single.fromCallable {
        validTimeZoneIDs
    }.subscribeOn(Schedulers.io())

    fun save(create: Boolean) = profileModel.observeSession()
        .takeSingle()
        .flatMap {
            if (it) {
                saveServer(create)
            } else {
                saveLocal()
            }
        }.react()


    private fun saveLocal() = timeEntry.takeSingle()
        .flatMap {
            Single.fromCallable {
                entryRepository.addEntry(it.get())
            }
        }
        .subscribeOn(Schedulers.io())

    private fun deleteLocal() = timeEntry.takeSingle()
        .flatMapCompletable {
            Completable.fromCallable {
                entryRepository.removeEntry(it.get())
            }
        }
        .subscribeOn(Schedulers.io())

    private fun saveServer(create: Boolean) = timeEntry.takeSingle().flatMap {
        val entry = it.get()

        val userId = if (user.value?.isPresent == true) {
            user.value!!.get().id
        } else {
            null
        }

        if (create) {
            entryRepository.createEntry(entry, userId = userId)
        } else {
            entryRepository.updateTime(entry.id, entry.timeZone, userId = userId)
        }
    }

    private fun deleteServer() = timeEntry.takeSingle()
        .flatMapCompletable {
            entryRepository.deleteEntry(it.get())
        }.react()

    fun delete() = profileModel.observeSession()
        .takeSingle()
        .flatMapCompletable {
            if (it) {
                deleteServer()
            } else {
                deleteLocal()
            }
        }

    fun loadUser(userId: Long?): Observable<Unit> {
        return userId?.let {
            usersRepository.user(it).doOnNext { user.onNext(Optional.of(it)) }.map { }
        } ?: Observable.empty()
    }
}