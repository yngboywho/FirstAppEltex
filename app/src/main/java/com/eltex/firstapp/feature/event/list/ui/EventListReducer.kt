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
    ): ReducerResult<EventListState, EventListEffect> = TODO()

    private companion object {
        const val PAGE_SIZE = 5
    }


}