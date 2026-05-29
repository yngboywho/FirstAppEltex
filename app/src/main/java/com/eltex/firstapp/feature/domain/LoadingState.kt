package com.eltex.firstapp.feature.domain

sealed interface LoadingState {
    data object Idle : LoadingState
    data object Loading : LoadingState
    data class Error(val value: Throwable) : LoadingState
}