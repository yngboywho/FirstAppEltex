package com.eltex.firstapp.feature.auth.ui

sealed interface AuthEffect {
    data object ShowSuccess : AuthEffect
}