package com.eltex.firstapp.feature.registration.ui

import com.eltex.firstapp.feature.registration.domain.ConfirmPasswordError
import com.eltex.firstapp.feature.registration.domain.LoginError
import com.eltex.firstapp.feature.registration.domain.PasswordError

data class RegistrationState(
    val login: String = "",
    val name: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val loginError: LoginError? = null,
    val passwordError: PasswordError? = null,
    val confirmPasswordError: ConfirmPasswordError? = null,
)
