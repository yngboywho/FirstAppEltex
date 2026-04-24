package com.eltex.firstapp.feature.post.ui

sealed interface EventListEffect {
    data class ScrollTo(val index: Int) :EventListEffect
}