package com.eltex.firstapp.feature.event.list.ui

import androidx.compose.runtime.Immutable
import com.eltex.firstapp.feature.domain.LoadingState

@Immutable
data class EventListState(
    val events: List<EventUiModel> = emptyList(),
    val status: LoadingState = LoadingState.Idle,
)