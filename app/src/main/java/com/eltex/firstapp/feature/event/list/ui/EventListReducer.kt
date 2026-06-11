package com.eltex.firstapp.feature.event.list.ui

import com.eltex.firstapp.domain.LoadingState
import com.eltex.firstapp.feature.event.list.ui.EventListEffect.*
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
        is EventListMessage.AddEvent -> ReducerResult(currentState, EventListMessage.AddEvent(message.text))
        is EventListMessage.Delete -> {
            val eventToDelete = currentState.events?.find { it.id == message }

            eventToDelete?.let
        }
        is EventListMessage.Like -> TODO()
        is EventListMessage.Participate -> TODO()
        is EventListMessage.SaveEdited -> TODO()
        EventListMessage.LoadInitial -> ReducerResult(
            newState = currentState.copy(status = LoadingState.Loading),
            effect = LoadInitial(PAGE_SIZE)
        )

        is EventListMessage.LoadInitialResult -> ReducerResult(
            message.result.fold(
            ifLeft = {
                currentState.copy(status = LoadingState.Error(it))
            },
            ifRight = { events ->
                currentState.copy(status = LoadingState.Idle, events = events.map { it.toUiModel() })
            }
        ))

        EventListMessage.LoadNextPage -> TODO()
        is EventListMessage.LoadNextPageResult -> TODO()
    }

    private companion object {
        const val PAGE_SIZE = 5
    }


}