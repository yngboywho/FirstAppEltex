package com.eltex.firstapp.feature.event.list.ui

import androidx.compose.runtime.Immutable
import com.eltex.firstapp.domain.LoadingState

@Immutable
data class EventListState(
    val events: List<EventUiModel>? = null,
    val status: LoadingState = LoadingState.Idle,
) {
    val isEmptyLoading = events == null && status == LoadingState.Loading
    val isEmptyError = events == null && status is LoadingState.Error
    val isRefreshing = events != null && status == LoadingState.Loading
}