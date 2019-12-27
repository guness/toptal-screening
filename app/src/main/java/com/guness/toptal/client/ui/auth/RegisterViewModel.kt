package com.guness.toptal.client.ui.auth

import com.guness.toptal.client.core.BaseViewModel
import com.guness.toptal.client.data.repositories.AuthRepository
import javax.inject.Inject

class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) : BaseViewModel() {
    fun register(name: String, username: String, password: String) = authRepository.register(name, username, password).react()
}