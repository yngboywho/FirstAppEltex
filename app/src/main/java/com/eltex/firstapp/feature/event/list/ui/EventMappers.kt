package com.eltex.firstapp.feature.event.list.ui

import com.eltex.firstapp.feature.event.domain.Event
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val displayFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")

fun Event.toUiModel(): EventUiModel {
    val dateTime = runCatching { Instant.parse(publishedAt).atZone(ZoneId.systemDefault()).toLocalDateTime() }
        .getOrElse {
            runCatching {
                ZonedDateTime.parse(publishedAt)
                    .withZoneSameInstant(ZoneId.systemDefault())
                    .toLocalDateTime()
            }.getOrNull()
        }
    return EventUiModel(
        id = id,
        publishedAt = dateTime ?: LocalDateTime.MIN,
        published = dateTime?.format(displayFormatter).orEmpty(),
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