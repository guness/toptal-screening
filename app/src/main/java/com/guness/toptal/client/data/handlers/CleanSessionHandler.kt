package com.guness.toptal.client.data.handlers

import androidx.annotation.WorkerThread
import com.guness.toptal.client.data.repositories.ProfileRepository
import com.guness.toptal.client.data.repositories.UsersRepository
import com.guness.toptal.client.data.room.UserDao
import javax.inject.Inject

class CleanSessionHandler @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userDao: UserDao
) {

    @WorkerThread
    fun clean() {
        profileRepository.clearProfile()
        userDao.clear()
    }
}