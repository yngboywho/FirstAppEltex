package com.eltex.firstapp.feature.post.domain

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long = 0,
    val publishedAt: String,
    val content: String = "",
    val author: String = "",
    val link: String = "",
    val likes: Int = 0,
    val likedByMe: Boolean = false,
)
