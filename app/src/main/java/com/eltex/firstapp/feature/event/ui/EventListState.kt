package com.eltex.firstapp.feature.event.ui

import androidx.compose.runtime.Immutable

@Immutable
data class EventListState(
    val events: List<EventUiModel> = emptyList(),
)