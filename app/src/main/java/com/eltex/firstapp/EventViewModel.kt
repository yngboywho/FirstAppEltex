package com.eltex.firstapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class EventViewModel : ViewModel() {
    var state by mutableStateOf(
        EventUiModel(
            author = "Lydia Westervelt",
            published = "11.05.22 11:21",
            status = "Offline",
            visit = "16.05.22 12:00",
            content = "Приглашаю провести уютный вечер за увлекательными играми! " +
                    "У нас есть несколько вариантов настолок, " +
                    "подходящих для любой компании.",
            link = "https://m2.material.io/components/cards",
            likes = 2,
            likedByMe = false,
            participants = 2,
            participantsByMe = false,
        )
    )
        private set

    fun like() {
        state = with(state) {
            copy(
                likedByMe = !likedByMe,
                likes = if (likedByMe) likes - 1 else likes + 1,
            )
        }
    }

    fun participate() {
        state = with(state) {
            copy(
                participantsByMe = !participantsByMe,
                participants = if (participantsByMe) participants - 1 else participants + 1,
            )
        }
    }

    fun share() {
        throw NotImplementedError("Поделиться не реализовано")
    }
}