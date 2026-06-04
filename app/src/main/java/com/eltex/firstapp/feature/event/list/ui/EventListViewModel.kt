package com.eltex.firstapp.feature.event.list.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eltex.firstapp.feature.domain.LoadingState
import com.eltex.firstapp.feature.event.domain.EventsRepository
import kotlinx.coroutines.launch

class EventListViewModel(
    private val repository: EventsRepository,
) : ViewModel() {
    var state by mutableStateOf(EventListState())
        private set

    init {
        loadEvents()
    }

    fun accept(message: EventListMessage) {
        when (message) {
            is EventListMessage.Like -> viewModelScope.launch {
                runCatching {
                    repository.likeById(
                        message.id,
                        message.likedByMe,
                    )
                }
                    .onSuccess {
                        state = state.copy(events = state.events.replaceById(it.toUiModel()))
                    }
            }

            is EventListMessage.Participate -> viewModelScope.launch {
                runCatching {
                    repository.participateById(
                        message.id,
                        message.participatedByMe,
                    )
                }
                    .onSuccess {
                        state = state.copy(events = state.events.replaceById(it.toUiModel()))
                    }
            }

            is EventListMessage.SaveEdited -> viewModelScope.launch {
                runCatching {
                    repository.update(
                        message.id,
                        message.content
                    )
                }
                    .onSuccess {
                        state = state.copy(events = state.events.replaceById(it.toUiModel()))
                    }
            }

            is EventListMessage.AddPost -> viewModelScope.launch {
                runCatching {
                    repository.save(
                        content = message.content
                    )
                }
                    .onSuccess { event ->
                        state = state.copy(events = buildList {
                            add(event.toUiModel())
                            addAll(state.events)
                        })
                    }
            }

            is EventListMessage.Delete -> viewModelScope.launch {
                runCatching {
                    repository.deleteById(message.id)
                }
                    .onSuccess {
                        state = state.copy(events = state.events.filter { it.id != message.id })
                    }
            }

            is EventListMessage.Retry -> loadEvents()
        }
    }

    private fun loadEvents() {
        state = state.copy(status = LoadingState.Loading)

        viewModelScope.launch {
            try {
                val events = repository.getEvents()

                state = state.copy(
                    events = events.map { it.toUiModel() },
                    status = LoadingState.Idle
                )
            } catch (error: Exception) {
                state = state.copy(status = LoadingState.Error(error))
            }
        }
    }

    fun findById(id: Long): EventUiModel? = state.events.find { it.id == id }

    private fun List<EventUiModel>.replaceById(updated: EventUiModel) =
        map { if (it.id == updated.id) updated else it }
}