package com.eltex.firstapp.feature.event.list.ui

sealed interface EventListEffect {
    data class LoadInitial(val size: Int): EventListEffect
    data class LoadNextPage(val id: Long, val size: Int) : EventListEffect

    data class Like(
        val liked: Boolean,
        val data: LikeData,
    ) : EventListEffect

    data class Participate(
        val participatedByMe: Boolean,
        val data: ParticipateData,
    ) : EventListEffect


    data class EditEvent(val id: Long, val content: String) : EventListEffect

    data class ScrollTo(val index: Int): EventListEffect
    data class Error(val value: Throwable): EventListEffect

    data class Delete(val original: EventUiModel) : EventListEffect
    data class AddEvent(val content: String) : EventListEffect
}

data class LikeData(
    val id: Long,
    val originalLikedByMe: Boolean,
    val originalLikes: Int,
)

data class ParticipateData(
    val id: Long,
    val originalParticipatedByMe: Boolean,
    val originalParticipants: Int,
)