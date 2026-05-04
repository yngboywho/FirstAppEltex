package com.eltex.firstapp.feature.post.ui

sealed interface PostListItem {
    data class DateSeparator(val label: String, val epochDay: Long) : PostListItem
    data class Post(val post: PostUiModel) : PostListItem
}
