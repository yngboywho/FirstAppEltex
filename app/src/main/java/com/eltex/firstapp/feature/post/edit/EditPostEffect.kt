package com.eltex.firstapp.feature.post.edit

sealed interface EditPostEffect {
    data class NavigateBack(val id: Long, val content: String): EditPostEffect
}