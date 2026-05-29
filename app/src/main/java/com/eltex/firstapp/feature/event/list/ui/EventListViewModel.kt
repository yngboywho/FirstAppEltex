package com.eltex.firstapp.feature.event.list.ui

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.eltex.firstapp.feature.domain.LoadingState
import com.eltex.firstapp.feature.event.domain.Callback
import com.eltex.firstapp.feature.event.domain.Event
import com.eltex.firstapp.feature.event.domain.EventsRepository
import com.eltex.firstapp.feature.util.AppSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class EventListViewModel(
    private val repository: EventsRepository,
    private val schedulers: AppSchedulers = AppSchedulers.DEFAULT
): ViewModel() {
    var state by mutableStateOf(EventListState())
        private set

    private val disposable = CompositeDisposable()

    init {
        loadEvents()
    }

    fun accept(message: EventListMessage) {
        when (message) {
            is EventListMessage.Like -> repository.likeById(
                message.id,
                message.likedByMe,
                )
                .observeOn(schedulers.main)
                .subscribeBy(
                    onSuccess = { state = state.copy(events = state.events.replaceById(it.toUiModel())) },
                    onError = {},
                )
                .addTo(disposable)

            is EventListMessage.Participate -> repository.participateById(
                message.id,
                message.participatedByMe,
                )
                .observeOn(schedulers.main)
                .subscribeBy(
                    onSuccess = { state = state.copy(events = state.events.replaceById(it.toUiModel())) },
                    onError = {},
                )
                .addTo(disposable)

            is EventListMessage.SaveEdited -> repository.update(
                message.id,
                message.content
                )
                .observeOn(schedulers.main)
                .subscribeBy(
                    onSuccess = { state = state.copy(events = state.events.replaceById(it.toUiModel())) },
                    onError = {},
                )
                .addTo(disposable)

            is EventListMessage.AddPost -> repository.save(
                content = message.content,
                author = "Me",
                status = "Online",
                visit = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd.MM.yy HH:mm", Locale.getDefault())),
            )
                .observeOn(schedulers.main)
                .subscribeBy(
                    onSuccess = { event ->
                        state = state.copy(events = buildList {
                            add(event.toUiModel())
                            addAll(state.events)
                        })
                    },
                    onError = {},
                )
                .addTo(disposable)

            is EventListMessage.Delete -> repository.deleteById(message.id)
                .subscribeBy(
                    onComplete = { state = state.copy(events = state.events.filter { it.id != message.id }) },
                    onError = {},
                )
                .addTo(disposable)

            is EventListMessage.Retry -> loadEvents()
        }
    }

    private fun loadEvents() {
        state = state.copy(status = LoadingState.Loading)
        repository.getEvents()
            .observeOn(schedulers.main)
            .subscribeBy(
                onSuccess = { state = state.copy(events = it.map { event -> event.toUiModel() }, status = LoadingState.Idle) },
                onError = { state = state.copy(status = LoadingState.Error(it)) },
            )
            .addTo(disposable)
    }

    fun findById(id: Long): EventUiModel? = state.events.find { it.id == id }

    override fun onCleared() {
        disposable.dispose()
    }

    private fun List<EventUiModel>.replaceById(updated: EventUiModel) =
        map { if (it.id == updated.id) updated else it }
}