package com.eltex.firstapp.feature.auth.ui

sealed interface AuthMessage {
    data class LoginChanged(val value: String) : AuthMessage
    data class PasswordChanged(val value: String) : AuthMessage
    data object Submit : AuthMessage
}