package com.guness.toptal.client.data.handlers

import androidx.annotation.WorkerThread
import com.guness.toptal.client.data.repositories.ProfileRepository
import javax.inject.Inject

class CleanSessionHandler @Inject constructor(private val profileRepository: ProfileRepository) {

    @WorkerThread
    fun clean() {
        profileRepository.clearProfile()
    }
}