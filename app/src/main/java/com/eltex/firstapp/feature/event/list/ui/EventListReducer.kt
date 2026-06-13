package com.eltex.firstapp.feature.event.list.ui

import com.eltex.firstapp.domain.LoadingState
import com.eltex.firstapp.feature.event.list.ui.EventListMessage.*
import com.eltex.firstapp.feature.tea.Reducer
import com.eltex.firstapp.feature.tea.ReducerResult
import java.time.ZoneId

class EventListReducer : Reducer<EventListState, EventListMessage, EventListEffect> {
    override fun reduce(
        currentState: EventListState,
        message: EventListMessage,
    ): ReducerResult<EventListState, EventListEffect> = when (message) {

        is EventListMessage.AddEvent -> ReducerResult(
            newState = currentState.copy(status = LoadingState.Loading),
            effect = EventListEffect.AddEvent(message.text)
        )

        is AddEventResult -> ReducerResult(
            message.value.fold(
                ifLeft = { currentState.copy(status = LoadingState.Error(it))},

                ifRight = { event ->
                    currentState.copy(
                        status = LoadingState.Idle,
                        events = listOf(event.toUiModel()) + currentState.events.orEmpty()
                    )
                }
            )
        )

        is EventListMessage.Delete -> {
            val original = currentState.events.orEmpty().find { it.id == message.id }
            if (original != null) {
                ReducerResult(
                    newState = currentState.copy(
                        events = currentState.events.orEmpty().filter { it.id != message.id }
                    ),
                    effect = EventListEffect.Delete(original)
                )
            } else {
                ReducerResult(currentState)
            }
        }

        is DeleteError -> ReducerResult(
            newState = currentState.copy(
                events = (currentState.events.orEmpty() + message.original)
                    .sortedByDescending { it.publishedAt }
            ),
            effect = EventListEffect.Error(message.error)
        )

        is EventListMessage.Like -> {
            val event = currentState.events.orEmpty().find { it.id == message.id }
            if (event != null) {
                ReducerResult(
                    newState = currentState.copy(
                        events = currentState.events.orEmpty().map {
                            if (it.id == message.id) it.copy(
                                likedByMe = !message.likedByMe,
                                likes = if (message.likedByMe) it.likes - 1 else it.likes + 1
                            ) else it
                        }
                    ),
                    effect = EventListEffect.Like(
                        liked = message.likedByMe,
                        data = LikeData(
                            id = message.id,
                            originalLikedByMe = event.likedByMe,
                            originalLikes = event.likes,
                        )
                    )
                )
            } else {
                ReducerResult(currentState)
            }
        }

        is LikeError -> ReducerResult(
            newState = currentState.copy(
                events = currentState.events.orEmpty().map {
                    if (it.id == message.eventId) it.copy(
                        likedByMe = message.originalLikedByMe,
                        likes = message.originalLikes,
                    ) else it
                }
            ),
            effect = EventListEffect.Error(message.error)
        )

        is LikeSuccess -> ReducerResult(
            newState = currentState.copy(
                events = currentState.events.orEmpty().map {
                    if (it.id == message.event.id) message.event.toUiModel() else it
                }
            )
        )

        EventListMessage.LoadInitial -> ReducerResult(
            newState = currentState.copy(status = LoadingState.Loading),
            effect = EventListEffect.LoadInitial(PAGE_SIZE)
        )

        is LoadInitialResult -> ReducerResult(
            message.result.fold(
            ifLeft = {
                currentState.copy(status = LoadingState.Error(it))
            },
            ifRight = { events ->
                currentState.copy(status = LoadingState.Idle, events = events.map { it.toUiModel() })
            }
        ))

        EventListMessage.LoadNextPage -> {
            val lastId = currentState.events.orEmpty().lastOrNull()?.id

            if (lastId == null || currentState.status == LoadingState.Loading) {
                ReducerResult(currentState)
            } else {
                ReducerResult(
                    newState = currentState.copy(status = LoadingState.Loading),
                    effect = EventListEffect.LoadNextPage(
                        currentState.events.orEmpty().first().id,
                        PAGE_SIZE
                    )
                )
            }
        }

        is LoadNextPageResult -> ReducerResult(
            message.result.fold(
                ifLeft = {
                    currentState.copy(status = LoadingState.Error(it))
                },
                ifRight = { events ->
                    currentState.copy(
                        status = LoadingState.Idle,
                        events = currentState.events.orEmpty() + events.map {
                            it.toUiModel()
                        }
                    )
                }
            ))

        is EventListMessage.Participate -> {
            val event = currentState.events.orEmpty().find { it.id == message.id }
            if (event != null) {
                ReducerResult(
                    newState = currentState.copy(
                        events = currentState.events.orEmpty().map {
                            if (it.id == message.id) it.copy(
                                participantsByMe = !message.participatedByMe,
                                participants = if (message.participatedByMe) it.participants - 1 else it.participants + 1
                            ) else it
                        }
                    ),
                    effect = EventListEffect.Participate(
                        participatedByMe = message.participatedByMe,
                        data = ParticipateData(
                            id = message.id,
                            originalParticipatedByMe = event.participantsByMe,
                            originalParticipants = event.participants,
                        )
                    )
                )
            } else {
                ReducerResult(currentState)
            }
        }

        is ParticipateError -> ReducerResult(
            newState = currentState.copy(
                events = currentState.events.orEmpty().map {
                    if (it.id == message.eventId) it.copy(
                        participantsByMe = message.originalParticipatedByMe,
                        participants = message.originalParticipants,
                    ) else it
                }
            ),
            effect = EventListEffect.Error(message.error)
        )

        is ParticipateSuccess -> ReducerResult(
            newState = currentState.copy(
                events = currentState.events.orEmpty().map {
                    if (it.id == message.event.id) message.event.toUiModel() else it
                }
            )
        )

        is SaveEdited -> ReducerResult(
            newState = currentState,
            effect = EventListEffect.EditEvent(message.id, message.content)
        )

        is SaveEditedResult -> ReducerResult(
            message.value.fold(
                ifLeft = {
                    currentState.copy(status = LoadingState.Error(it))
                },
                ifRight = { event ->
                    currentState.copy(
                        events = currentState.events.orEmpty().map {
                            if (it.id == event.id) event.toUiModel() else it
                        }
                    )
                }
            )
        )

    }

    private companion object {
        const val PAGE_SIZE = 5
    }
}