package com.eltex.firstapp.feature.post.ui

sealed interface PostListMessage {
    data class Like(val id: Long) : PostListMessage
    data class SaveEdited(val id: Long, val content: String) : PostListMessage
    data class AddPost(val content: String) : PostListMessage
    data class Delete(val id: Long) : PostListMessage
}
