package com.eltex.firstapp.feature.event.ui

import com.eltex.firstapp.feature.event.domain.Event
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val displayFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm", Locale.getDefault())

fun Event.toUiModel(): EventUiModel {
    val dateTime = runCatching { LocalDateTime.parse(publishedAt) }.getOrDefault(LocalDateTime.now())
    return EventUiModel(
        id = id,
        publishedAt = dateTime,
        published = dateTime.format(displayFormatter),
        status = status,
        visit = visit,
        content = content,
        author = author,
        link = link,
        likes = likes,
        likedByMe = likedByMe,
        participants = participants,
        participantsByMe = participantsByMe,
    )
}