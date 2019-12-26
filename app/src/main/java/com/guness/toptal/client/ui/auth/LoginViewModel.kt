package com.guness.toptal.client.ui.auth

import com.guness.toptal.client.core.BaseViewModel
import com.guness.toptal.client.data.repositories.AuthRepository
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : BaseViewModel() {

    fun login(username: String, password: String) = authRepository.login(username, password).react()
}

