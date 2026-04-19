package com.eltex.firstapp.feature.post

import androidx.compose.runtime.Immutable

@Immutable
data class EventListState(
    val events: List<EventUiModel> = emptyList(),
)