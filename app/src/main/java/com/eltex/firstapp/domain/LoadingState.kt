package com.eltex.firstapp.domain

sealed interface LoadingState {
    data object Idle : LoadingState
    data object Loading : LoadingState
    data class Error(val value: Throwable) : LoadingState
}