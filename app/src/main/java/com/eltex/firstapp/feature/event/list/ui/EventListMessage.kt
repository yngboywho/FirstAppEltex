package com.eltex.firstapp.feature.event.list.ui

sealed interface EventListMessage {
    data class Like(val id: Long, val likedByMe: Boolean): EventListMessage
    data class Participate(val id: Long, val participatedByMe: Boolean): EventListMessage
    data class SaveEdited(val id: Long, val content: String) : EventListMessage
    data class AddEvent(val content: String) : EventListMessage
    data class Delete(val id: Long): EventListMessage
    data object Retry : EventListMessage
}