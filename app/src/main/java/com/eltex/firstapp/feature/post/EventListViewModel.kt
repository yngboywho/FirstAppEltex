package com.eltex.firstapp.feature.post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.time.LocalDateTime

class EventListViewModel: ViewModel() {
    var state by mutableStateOf(
        EventListState(List(100, ::createEvent))
    )
        private set

    fun accept(message: EventListMessage) {
        state = reduce(state, message)
    }

    private fun reduce(
        current: EventListState,
        message: EventListMessage,
    ): EventListState = when (message){
        is EventListMessage.Like -> current.copy(events = current.events.toggleLike(message.id))
        is EventListMessage.Participate -> current.copy(events = current.events.toggleParticipate(message.id))
    }

    private fun List<EventUiModel>.toggleLike(id: Long) = map { event ->
        if (event.id != id) event
        else event.copy(
            likedByMe = !event.likedByMe,
            likes = if (event.likedByMe) event.likes - 1 else event.likes + 1,
        )
    }

    private fun List<EventUiModel>.toggleParticipate(id: Long) = map { event ->
        if (event.id != id) event
        else event.copy(
            participantsByMe = !event.participantsByMe,
            participants = if (event.participantsByMe) event.participants - 1 else event.participants + 1,
        )
    }

    private fun createEvent(count: Int): EventUiModel {
        val daysAgo = count/20L
        val publishedAt = LocalDateTime.now()
            .minusDays(daysAgo)
            .withHour(10 + (count % 8))
            .withMinute((count * 7) % 60)
            .withSecond(0)
            .withNano(0)

        return EventUiModel(
            id = count.toLong() + 1,
            publishedAt = publishedAt,
            published = publishedAt.toString(),
            status = if (count % 2 == 0) "Offline" else "Online",
            visit = publishedAt.plusHours(2).plusDays(5).toString(),
            content = "Приглашаю провести уютный вечер за увлекательными играми! " +
                    "У нас есть несколько вариантов настолок, " +
                    "подходящих для любой компании.",
            author = "Lydia Westervelt",
            link = "https://m2.material.io/components/cards",
            likes = count % 10,
            likedByMe = count % 3 == 0,
            participants = count % 7,
            participantsByMe = count % 4 == 0,
        )
    }
}