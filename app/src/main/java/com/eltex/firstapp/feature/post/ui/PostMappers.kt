package com.eltex.firstapp.feature.post.ui

import com.eltex.firstapp.feature.post.domain.Post
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val postDisplayFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm", Locale.getDefault())

fun Post.toUiModel(): PostUiModel {
    val dateTime = runCatching { LocalDateTime.parse(publishedAt) }.getOrDefault(LocalDateTime.now())
    return PostUiModel(
        id = id,
        publishedAt = dateTime,
        published = dateTime.format(postDisplayFormatter),
        content = content,
        author = author,
        link = link,
        likes = likes,
        likedByMe = likedByMe,
    )
}
