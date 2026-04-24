package com.eltex.firstapp.feature.auth.domain

sealed interface AuthLoginError
object AuthLoginEmpty : AuthLoginError

sealed interface AuthPasswordError
object AuthPasswordEmpty : AuthPasswordError
object AuthPasswordTooShort : AuthPasswordError

fun validateAuthLogin(login: String): AuthLoginError? =
    if (login.isBlank()) AuthLoginEmpty else null

fun validateAuthPassword(password: String): AuthPasswordError? = when {
    password.isBlank() -> AuthPasswordEmpty
    password.length < 6 -> AuthPasswordTooShort
    else -> null
}