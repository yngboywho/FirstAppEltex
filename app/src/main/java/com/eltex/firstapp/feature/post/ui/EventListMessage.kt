package com.eltex.firstapp.feature.post.ui

sealed interface EventListMessage {
    data class Like(val id: Long): EventListMessage
    data class Participate(val id: Long): EventListMessage
    data class SaveEdited(val id: Long, val content: String) : EventListMessage
    data class AddPost(val content: String) : EventListMessage
    data class Delete(val id: Long): EventListMessage
}