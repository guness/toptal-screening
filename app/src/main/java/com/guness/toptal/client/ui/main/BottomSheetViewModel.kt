package com.guness.toptal.client.ui.main

import com.guness.toptal.client.core.BaseViewModel
import com.guness.toptal.client.data.repositories.ProfileRepository
import javax.inject.Inject

class BottomSheetViewModel @Inject constructor(profileRepository: ProfileRepository) : BaseViewModel() {
    val hasSession = profileRepository.observeSession()
    val isAdmin = profileRepository.observeAdmin()
}