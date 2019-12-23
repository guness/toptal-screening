package com.guness.toptal.client.ui.main

import com.guness.toptal.client.core.BaseViewModel
import com.guness.toptal.client.data.repositories.AuthRepository
import com.guness.toptal.client.data.repositories.EntryRepository
import com.guness.toptal.client.data.repositories.ProfileRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val entryRepository: EntryRepository,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository
) : BaseViewModel() {

    val entries = entryRepository.entries()

    override fun onStart() {
        profileRepository.observeSession()
            .filter { it }
            .flatMapMaybe { entryRepository.fetchEntries().react() }
            .subs()
    }

    fun login() {
        authRepository.login("user", "password").subs()
    }
}
