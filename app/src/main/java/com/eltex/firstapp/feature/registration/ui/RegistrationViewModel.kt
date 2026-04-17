package com.eltex.firstapp.feature.registration.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.eltex.firstapp.feature.registration.domain.validateLogin
import com.eltex.firstapp.feature.registration.domain.validatePassword
import com.eltex.firstapp.feature.registration.domain.validatePasswordConfirm
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class RegistrationViewModel: ViewModel() {
    var state by mutableStateOf(RegistrationState())
        private set
    private val _effects = MutableSharedFlow<RegistrationEffect>(
        extraBufferCapacity = 1
    )
    val effects = _effects.asSharedFlow()

    fun accept(message: RegistrationMessage) {
        state = reduce(state, message)
    }

    private fun reduce(
        current: RegistrationState,
        message: RegistrationMessage,
    ): RegistrationState = when (message) {

        is RegistrationMessage.LoginChanged ->
            current.copy(login = message.value, loginError = null)

        is RegistrationMessage.NameChanged ->
            current.copy(name = message.value)

        is RegistrationMessage.PasswordChanged ->
            current.copy(password = message.value, passwordError = null)

        is RegistrationMessage.ConfirmPasswordChanged ->
            current.copy(confirmPassword = message.value, confirmPasswordError = null)

        RegistrationMessage.Submit -> {
            val loginError   = validateLogin(current.login)
            val passwordError = validatePassword(current.password)
            val confirmError  = validatePasswordConfirm(current.password, current.confirmPassword)

            val newState = current.copy(
                loginError           = loginError,
                passwordError        = passwordError,
                confirmPasswordError = confirmError,
            )

            if (loginError == null && passwordError == null && confirmError == null) {
                _effects.tryEmit(RegistrationEffect.NavigateToFeed)
            }

            newState
        }
    }
}