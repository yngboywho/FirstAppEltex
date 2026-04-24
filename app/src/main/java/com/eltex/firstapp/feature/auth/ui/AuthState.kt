package com.eltex.firstapp.feature.auth.ui

import com.eltex.firstapp.feature.auth.domain.AuthLoginError
import com.eltex.firstapp.feature.auth.domain.AuthPasswordError

data class AuthState(
    val login: String = "",
    val password: String = "",
    val loginError: AuthLoginError? = null,
    val passwordError: AuthPasswordError? = null,
)