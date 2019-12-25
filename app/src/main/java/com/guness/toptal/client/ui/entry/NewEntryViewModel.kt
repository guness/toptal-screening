package com.guness.toptal.client.ui.entry

import com.guness.toptal.client.core.BaseViewModel
import com.guness.toptal.client.data.repositories.AuthRepository
import com.guness.toptal.client.data.repositories.EntryRepository
import com.guness.toptal.client.data.repositories.ProfileRepository
import com.guness.toptal.protocol.dto.TimeEntry
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.joda.time.DateTimeZone
import java.util.*
import javax.inject.Inject

class NewEntryViewModel @Inject constructor(
    private val entryRepository: EntryRepository,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : BaseViewModel() {

    val timeZone = BehaviorSubject.createDefault<Optional<DateTimeZone>>(Optional.empty())

    fun save() = profileRepository.observeSession()
        .take(1)
        .singleOrError()
        .flatMap {
            if (it) {
                saveServer()
            } else {
                saveLocal()
            }
        }
        .react()


    private fun saveLocal() = Single.fromCallable {
        entryRepository.addEntry(TimeEntry(timeZone = timeZone.value!!.get()))
    }.subscribeOn(Schedulers.io())

    private fun saveServer() = entryRepository.createEntry(TimeEntry(timeZone = timeZone.value!!.get()))
}