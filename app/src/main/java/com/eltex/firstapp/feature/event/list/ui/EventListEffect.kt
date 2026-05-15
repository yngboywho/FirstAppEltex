package com.eltex.firstapp.feature.event.list.ui

sealed interface EventListEffect {
    data class ScrollTo(val index: Int) :EventListEffect
}