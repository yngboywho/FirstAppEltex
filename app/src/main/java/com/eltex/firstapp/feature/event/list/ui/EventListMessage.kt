package com.eltex.firstapp.feature.event.list.ui

import arrow.core.Either
import com.eltex.firstapp.domain.AppException
import com.eltex.firstapp.feature.event.domain.Event

sealed interface EventListMessage {
    data object LoadInitial: EventListMessage
    data object LoadNextPage: EventListMessage
    data class LoadInitialResult(
        val result: Either<AppException, List<Event>>,
    ): EventListMessage

    data class LoadNextPageResult(
        val result: Either<AppException, List<Event>>,
    ) : EventListMessage

    data class Like(val id: Long, val likedByMe: Boolean): EventListMessage
    data class LikeError(
        val eventId: Long,
        val originalLikedByMe: Boolean,
        val originalLikes: Int,
        val error: AppException,
    ) : EventListMessage

    data class LikeSuccess(val event: Event) : EventListMessage

    data class Participate(val id: Long, val participatedByMe: Boolean) : EventListMessage
    data class ParticipateError(
        val eventId: Long,
        val originalParticipatedByMe: Boolean,
        val originalParticipants: Int,
        val error: AppException,
    ) : EventListMessage

    data class ParticipateSuccess(val event: Event) : EventListMessage


    data class SaveEdited(val id: Long, val content: String) : EventListMessage
    data class SaveEditedResult(val value: Either<AppException, Event>) : EventListMessage

    data class AddEvent(val text: String) : EventListMessage
    data class AddEventResult(val value: Either<AppException, Event>): EventListMessage

    data class Delete(val id: Long): EventListMessage
    data class DeleteError(val original: EventUiModel, val error: AppException): EventListMessage
}