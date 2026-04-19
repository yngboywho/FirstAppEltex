package com.eltex.firstapp.feature.post

sealed interface EventListMessage {
    data class Like(val id: Long): EventListMessage
    data class Participate(val id: Long): EventListMessage
}