package com.eltex.firstapp.feature.post.add

interface AddPostMessage {
    data object Save: AddPostMessage
    data class TextChanged(val value: String): AddPostMessage
}