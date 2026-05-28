package com.eltex.firstapp.feature.event.list.ui

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.eltex.firstapp.feature.event.domain.Callback
import com.eltex.firstapp.feature.event.domain.Event
import com.eltex.firstapp.feature.event.domain.EventsRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class EventListViewModel(
    private val repository: EventsRepository,
): ViewModel() {
    var state by mutableStateOf(EventListState())
        private set

    private val mainHandler = Handler(Looper.getMainLooper())

    init {
        loadEvents()
    }

    fun accept(message: EventListMessage) {
        when (message) {
            is EventListMessage.Like -> repository.likeById(
                message.id,
                message.likedByMe,
                object : Callback<Event> {
                    override fun onSuccess(value: Event) = mainHandler.post {
                        state = state.copy(events = state.events.replaceById(value.toUiModel()))
                    }.let {}

                    override fun onError(error: Exception) = Unit
            })

            is EventListMessage.Participate -> repository.participateById(
                message.id,
                message.participatedByMe,
                object : Callback<Event> {
                    override fun onSuccess(value: Event) = mainHandler.post {
                        state = state.copy(events = state.events.replaceById(value.toUiModel()))
                    }.let {}

                    override fun onError(error: Exception) = Unit
            })

            is EventListMessage.SaveEdited -> repository.update(message.id, message.content, object : Callback<Event> {
                    override fun onSuccess(value: Event) = mainHandler.post {
                        state = state.copy(events = state.events.replaceById(value.toUiModel()))
                    }.let {}

                    override fun onError(error: Exception) = Unit
            })

            is EventListMessage.AddPost -> repository.save(
                content = message.content,
                author = "Me",
                status = "Online",
                visit = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd.MM.yy HH:mm", Locale.getDefault())),
                callback = object : Callback<Event> {
                    override fun onSuccess(value: Event) = mainHandler.post {
                        state = state.copy(events = buildList {
                            add(value.toUiModel())
                            addAll(state.events)
                        })
                    }.let {}
                    override fun onError(error: Exception) = Unit
                }
            )

            is EventListMessage.Delete -> repository.deleteById(message.id, object : Callback<Unit> {
                    override fun onSuccess(value: Unit) = mainHandler.post {
                        state = state.copy(events = state.events.filter { it.id != message.id })
                    }.let {}

                    override fun onError(error: Exception) = Unit
            })
        }
    }

    private fun loadEvents() {
        repository.getEvents(object: Callback<List<Event>> {
                override fun onSuccess(value: List<Event>) = mainHandler.post {
                    state = state.copy(events = value.map { it.toUiModel() })
                }.let {}

                override fun onError(error: Exception) = Unit

        })
    }

    fun findById(id: Long): EventUiModel? = state.events.find { it.id == id }

    private fun List<EventUiModel>.replaceById(updated: EventUiModel) =
        map { if (it.id == updated.id) updated else it }
}