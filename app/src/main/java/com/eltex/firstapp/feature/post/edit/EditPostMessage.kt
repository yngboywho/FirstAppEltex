package com.eltex.firstapp.feature.post.edit

sealed interface EditPostMessage {
    data class ContentChanged(val value: String): EditPostMessage
    data object Save: EditPostMessage
}