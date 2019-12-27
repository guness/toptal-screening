package com.guness.toptal.client.data.handlers

import androidx.annotation.WorkerThread
import com.guness.toptal.client.data.repositories.ProfileModel
import com.guness.toptal.client.data.room.UserDao
import javax.inject.Inject

class CleanSessionHandler @Inject constructor(
    private val profileModel: ProfileModel,
    private val userDao: UserDao
) {

    @WorkerThread
    fun clean() {
        profileModel.clear()
        userDao.clear()
    }
}