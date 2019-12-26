package com.guness.toptal.client.ui.entry

import com.guness.toptal.client.core.BaseViewModel
import com.guness.toptal.client.data.repositories.EntryRepository
import com.guness.toptal.client.data.repositories.ProfileRepository
import com.guness.toptal.client.data.repositories.UsersRepository
import com.guness.toptal.client.utils.extensions.takeSingle
import com.guness.toptal.protocol.dto.TimeEntry
import io.reactivex.Completable
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
    private val profileRepository: ProfileRepository
) : BaseViewModel() {

    val initialEntry = BehaviorSubject.createDefault<Optional<TimeEntry>>(Optional.empty())

    val timeZone = BehaviorSubject.createDefault<Optional<DateTimeZone>>(Optional.empty())

    val user = Observables.combineLatest(profileRepository.observeManager().filter { it }, initialEntry.filter { it.isPresent })
        .flatMap {
            usersRepository.user(it.second.get().userId!!)
        }

    private val timeEntry = Observables.combineLatest(timeZone, initialEntry)
        .map { pair ->
            pair.first.map {
                pair.second.orElseGet { TimeEntry(timeZone = DateTimeZone.UTC) }.copy(timeZone = it)
            }
        }

    fun save(create: Boolean) = profileRepository.observeSession()
        .takeSingle()
        .flatMap {
            if (it) {
                saveServer(create)
            } else {
                saveLocal()
            }
        }
        .react()


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
        if (create) {
            entryRepository.createEntry(entry)
        } else {
            entryRepository.updateTime(entry.id, entry.timeZone)
        }
    }

    private fun deleteServer() = timeEntry.takeSingle()
        .flatMapCompletable {
            entryRepository.deleteEntry(it.get())
        }

    fun delete() = profileRepository.observeSession()
        .takeSingle()
        .flatMapCompletable {
            if (it) {
                deleteServer()
            } else {
                deleteLocal()
            }
        }
}