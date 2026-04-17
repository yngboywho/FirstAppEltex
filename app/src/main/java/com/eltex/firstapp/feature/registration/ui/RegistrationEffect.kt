package com.eltex.firstapp.feature.registration.ui

sealed interface RegistrationEffect {
    data object NavigateToFeed: RegistrationEffect
}