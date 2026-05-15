package com.eltex.firstapp.feature.event.list.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.eltex.firstapp.feature.event.domain.EventsRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class EventListViewModel(
    private val repository: EventsRepository,
): ViewModel() {
    var state by mutableStateOf(
        EventListState(repository.getAll().map { it.toUiModel() })
    )
        private set

    fun accept(message: EventListMessage) {
        state = reduce(state, message)
    }

    private fun reduce(
        current: EventListState,
        message: EventListMessage,
    ): EventListState = when (message){
        is EventListMessage.Like -> {
            val updated = repository.likeById(message.id)
            current.copy(events = current.events.replaceById(updated.toUiModel()))
        }

        is EventListMessage.Participate -> {
            val updated = repository.participateById(message.id)
            current.copy(events = current.events.replaceById(updated.toUiModel()))
        }

        is EventListMessage.SaveEdited -> {
            val updated = repository.update(message.id, message.content)
            current.copy(events = current.events.replaceById(updated.toUiModel()))
        }

        is EventListMessage.AddPost -> {
            val saved = repository.save(
                content = message.content,
                author = "Me",
                status = "Online",
                visit = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd.MM.yy HH:mm", Locale.getDefault()))
            )
            current.copy(events = buildList {
                add(saved.toUiModel())
                addAll(current.events)
            })
        }

        is EventListMessage.Delete -> {
            repository.deleteById(message.id)
            current.copy(events = current.events.filter { it.id != message.id })
        }
    }

    fun findById(id: Long): EventUiModel? = state.events.find { it.id == id }

    private fun List<EventUiModel>.replaceById(updated: EventUiModel) =
        map { if (it.id == updated.id) updated else it }
}