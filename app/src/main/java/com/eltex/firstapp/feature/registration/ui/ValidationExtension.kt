package com.eltex.firstapp.feature.registration.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.eltex.firstapp.R
import com.eltex.firstapp.feature.registration.domain.ConfirmPasswordError
import com.eltex.firstapp.feature.registration.domain.LoginEmpty
import com.eltex.firstapp.feature.registration.domain.LoginError
import com.eltex.firstapp.feature.registration.domain.LoginTooShort
import com.eltex.firstapp.feature.registration.domain.PasswordEmpty
import com.eltex.firstapp.feature.registration.domain.PasswordError
import com.eltex.firstapp.feature.registration.domain.PasswordMismatch
import com.eltex.firstapp.feature.registration.domain.PasswordTooLong
import com.eltex.firstapp.feature.registration.domain.PasswordTooShort

@Composable
fun LoginError?.toReadableString(): String? = when (this) {
    LoginEmpty -> stringResource(R.string.reg_login_empty_error)
    LoginTooShort -> stringResource(R.string.reg_login_too_short_error)
    null          -> null
}

@Composable
fun PasswordError?.toReadableString(): String? = when (this) {
    PasswordEmpty -> stringResource(R.string.reg_password_empty_error)
    PasswordTooShort -> stringResource(R.string.reg_password_too_short_error)
    PasswordTooLong -> stringResource(R.string.reg_password_too_long_error)
    null             -> null
}

@Composable
fun ConfirmPasswordError?.toReadableString(): String? = when (this) {
    PasswordMismatch -> stringResource(R.string.reg_passwords_mismatch_error)
    null             -> null
}