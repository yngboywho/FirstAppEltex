package com.eltex.firstapp.feature.auth.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.eltex.firstapp.feature.auth.domain.validateAuthLogin
import com.eltex.firstapp.feature.auth.domain.validateAuthPassword
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AuthViewModel : ViewModel() {

    var state by mutableStateOf(AuthState())
        private set

    private val _effects = MutableSharedFlow<AuthEffect>(extraBufferCapacity = 1)
    val effects = _effects.asSharedFlow()

    fun accept(message: AuthMessage) {
        state = reduce(state, message)
    }

    private fun reduce(
        current: AuthState,
        message: AuthMessage,
    ): AuthState = when (message) {
        is AuthMessage.LoginChanged ->
            current.copy(login = message.value, loginError = null)

        is AuthMessage.PasswordChanged ->
            current.copy(password = message.value, passwordError = null)

        AuthMessage.Submit -> {
            val loginError = validateAuthLogin(current.login)
            val passwordError = validateAuthPassword(current.password)

            val newState = current.copy(
                loginError = loginError,
                passwordError = passwordError,
            )

            if (loginError == null && passwordError == null) {
                _effects.tryEmit(AuthEffect.ShowSuccess)
            }

            newState
        }
    }
}