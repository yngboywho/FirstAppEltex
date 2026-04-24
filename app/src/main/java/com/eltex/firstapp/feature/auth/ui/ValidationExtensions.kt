package com.eltex.firstapp.feature.auth.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.eltex.firstapp.R
import com.eltex.firstapp.feature.auth.domain.AuthLoginEmpty
import com.eltex.firstapp.feature.auth.domain.AuthLoginError
import com.eltex.firstapp.feature.auth.domain.AuthPasswordEmpty
import com.eltex.firstapp.feature.auth.domain.AuthPasswordError
import com.eltex.firstapp.feature.auth.domain.AuthPasswordTooShort

@Composable
fun AuthLoginError?.toReadableString(): String? = when (this) {
    AuthLoginEmpty -> stringResource(R.string.reg_login_empty_error)
    null -> null
}

@Composable
fun AuthPasswordError?.toReadableString(): String? = when (this) {
    AuthPasswordEmpty -> stringResource(R.string.reg_password_empty_error)
    AuthPasswordTooShort -> stringResource(R.string.reg_password_too_short_error)
    null -> null
}