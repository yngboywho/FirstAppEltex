package com.eltex.firstapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EventViewModel : ViewModel() {
    private val _state = MutableStateFlow(
        EventUiModel(
            author = "Lydia Westervelt",
            published = "11.05.22 11:21",
            status = "Offline",
            visit = "16.05.22 12:00",
            content = "Приглашаю провести уютный вечер за увлекательными играми! " +
                    "У нас есть несколько вариантов настолок, " +
                    "подходящих для любой компании.",
            likes = 2,
            likedByMe = false,
            participants = 2,
            participantsByMe = false,
        )
    )
    val state: StateFlow<EventUiModel> = _state

    var states = _state.value

    fun like() {
        states = with(states) {
            copy(
                likedByMe = !likedByMe,
                likes = if (likedByMe) likes - 1 else likes + 1,
            )
        }
    }

    fun participate() {
        states = with(states) {
            copy(
                participantsByMe = !participantsByMe,
                participants = if (participantsByMe) participants - 1 else participants + 1,
            )
        }
    }

    fun share() {
        throw NotImplementedError("Поделиться не реализовано сейчас")
    }
}