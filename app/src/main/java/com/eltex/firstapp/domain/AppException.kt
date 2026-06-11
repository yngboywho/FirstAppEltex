package com.eltex.firstapp.domain

sealed class AppException: Exception() {
    class Forbidden : AppException()

    class NetworkException : AppException()

    data class UnknownException(
        val code: Int,
        override val message: String?,
    ) : AppException()
}