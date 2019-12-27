package com.guness.toptal.client.ui.main

import com.guness.toptal.client.core.BaseViewModel
import com.guness.toptal.client.data.repositories.ProfileModel
import javax.inject.Inject

class BottomSheetViewModel @Inject constructor(profileModel: ProfileModel) : BaseViewModel() {
    val hasSession = profileModel.observeSession()
    val isAdmin = profileModel.observeAdmin()
}