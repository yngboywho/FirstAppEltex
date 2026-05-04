package com.eltex.firstapp.feature.post.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.eltex.firstapp.feature.post.domain.PostsRepository

class PostListViewModel(
    private val repository: PostsRepository,
) : ViewModel() {
    var state by mutableStateOf(
        PostListState(repository.getAll().map { it.toUiModel() })
    )
        private set

    fun accept(message: PostListMessage) {
        state = reduce(state, message)
    }

    private fun reduce(
        current: PostListState,
        message: PostListMessage,
    ): PostListState = when (message) {
        is PostListMessage.Like -> {
            val updated = repository.likeById(message.id)
            current.copy(posts = current.posts.replaceById(updated.toUiModel()))
        }

        is PostListMessage.SaveEdited -> {
            val updated = repository.update(message.id, message.content)
            current.copy(posts = current.posts.replaceById(updated.toUiModel()))
        }

        is PostListMessage.AddPost -> {
            val saved = repository.save(content = message.content, author = "Me")
            current.copy(posts = buildList {
                add(saved.toUiModel())
                addAll(current.posts)
            })
        }

        is PostListMessage.Delete -> {
            repository.deleteById(message.id)
            current.copy(posts = current.posts.filter { it.id != message.id })
        }
    }

    fun findById(id: Long): PostUiModel? = state.posts.find { it.id == id }

    private fun List<PostUiModel>.replaceById(updated: PostUiModel) =
        map { if (it.id == updated.id) updated else it }
}
