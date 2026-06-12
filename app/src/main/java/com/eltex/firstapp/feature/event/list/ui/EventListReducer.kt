package com.eltex.firstapp.feature.event.list.ui

import com.eltex.firstapp.domain.LoadingState
import com.eltex.firstapp.feature.event.list.ui.EventListEffect.*
import com.eltex.firstapp.feature.event.list.ui.EventListMessage.*
import com.eltex.firstapp.feature.tea.Reducer
import com.eltex.firstapp.feature.tea.ReducerResult
import java.time.ZoneId

class EventListReducer(
    private val zoneId: ZoneId = ZoneId.systemDefault(),
) : Reducer<EventListState, EventListMessage, EventListEffect> {
    override fun reduce(
        currentState: EventListState,
        message: EventListMessage,
    ): ReducerResult<EventListState, EventListEffect> = when (message) {

        is AddEvent -> TODO()

        is AddEventResult -> TODO()

        is EventListMessage.Delete -> TODO()

        is DeleteError -> TODO()

        is EventListMessage.Like -> TODO()

        is LikeError -> TODO()

        is LikeSuccess -> TODO()

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

        is Participate -> TODO()

        is SaveEdited -> TODO()
    }

    private companion object {
        const val PAGE_SIZE = 5
    }
}