package com.eltex.firstapp.feature.post.ui

sealed interface EventListItem {
    data class DateSeparator(val label: String, val epochDay: Long): EventListItem
    data class Event(val event: EventUiModel): EventListItem
}