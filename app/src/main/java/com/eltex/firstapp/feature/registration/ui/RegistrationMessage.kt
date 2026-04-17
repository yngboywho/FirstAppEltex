package com.eltex.firstapp.feature.registration.ui

sealed interface RegistrationMessage {
    data class LoginChanged(val value: String): RegistrationMessage
    data class NameChanged(val value: String): RegistrationMessage
    data class PasswordChanged(val value: String): RegistrationMessage
    data class ConfirmPasswordChanged(val value: String): RegistrationMessage
    data object Submit: RegistrationMessage
}