package com.eltex.firstapp.feature.registration.domain

sealed interface LoginError
object LoginEmpty: LoginError
object LoginTooShort: LoginError

sealed interface PasswordError
object PasswordEmpty: PasswordError
object PasswordTooShort: PasswordError
object PasswordTooLong: PasswordError

sealed interface ConfirmPasswordError
object PasswordMismatch: ConfirmPasswordError

fun validateLogin(login: String): LoginError? = when {
    login.isBlank() -> LoginEmpty
    login.length < 3 -> LoginTooShort
    else -> null
}

fun validatePassword(password: String): PasswordError? = when {
    password.isBlank() -> PasswordEmpty
    password.length <= 8 -> PasswordTooShort
    password.length > 128 -> PasswordTooLong
    else -> null
}

fun validatePasswordConfirm(password: String, confirm: String): ConfirmPasswordError? =
    if (password != confirm) PasswordMismatch else null