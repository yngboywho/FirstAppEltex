package com.eltex.firstapp.feature.event.domain

interface Callback<T> {
    fun onSuccess(value: T)
    fun onError(error: Exception)
}
