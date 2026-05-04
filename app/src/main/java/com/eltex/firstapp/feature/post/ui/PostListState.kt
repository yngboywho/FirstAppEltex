package com.eltex.firstapp.feature.post.ui

import androidx.compose.runtime.Immutable

@Immutable
data class PostListState(
    val posts: List<PostUiModel> = emptyList(),
)
