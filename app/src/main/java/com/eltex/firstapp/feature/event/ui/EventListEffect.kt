package com.eltex.firstapp.feature.event.ui

sealed interface EventListEffect {
    data class ScrollTo(val index: Int) :EventListEffect
}