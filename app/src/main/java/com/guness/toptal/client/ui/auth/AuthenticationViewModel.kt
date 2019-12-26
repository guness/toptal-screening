package com.guness.toptal.client.ui.auth

import com.guness.toptal.client.core.BaseViewModel
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class AuthenticationViewModel @Inject constructor() : BaseViewModel() {
    val authMode = BehaviorSubject.create<AuthMode>()
}