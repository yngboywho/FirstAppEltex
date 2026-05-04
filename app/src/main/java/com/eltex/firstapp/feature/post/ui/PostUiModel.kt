package com.eltex.firstapp.feature.post.ui

import java.time.LocalDateTime

data class PostUiModel(
    val id: Long = 0,
    val publishedAt: LocalDateTime = LocalDateTime.now(),
    val published: String = "",
    val content: String = "",
    val author: String = "",
    val link: String = "",
    val likes: Int = 0,
    val likedByMe: Boolean = false,
)
