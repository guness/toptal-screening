package com.guness.toptal.client.ui.main

import com.guness.toptal.client.core.BaseViewModel
import com.guness.toptal.client.data.repositories.ProfileRepository
import com.guness.toptal.protocol.dto.UserRole
import javax.inject.Inject

class BottomSheetViewModel @Inject constructor(profileRepository: ProfileRepository) : BaseViewModel() {
    val hasSession = profileRepository.observeSession()
    val isManager = profileRepository.observeProfile()
        .map {
            if (it.isPresent) {
                when (it.get().role) {
                    UserRole.ROLE_USER -> false
                    UserRole.ROLE_MANAGER,
                    UserRole.ROLE_ADMIN -> true
                }
            } else {
                false
            }
        }
}