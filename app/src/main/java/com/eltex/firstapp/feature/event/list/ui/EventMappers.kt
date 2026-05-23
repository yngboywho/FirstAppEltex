package com.eltex.firstapp.feature.event.list.ui

import com.eltex.firstapp.feature.event.domain.Event
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private val displayFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")

fun Event.toUiModel(): EventUiModel {
    val dateTime = runCatching { LocalDateTime.parse(publishedAt) }.getOrDefault(LocalDateTime.now())
//    val dateTime = runCatching { Instant.parse(publishedAt).atZone(ZoneId.systemDefault()) }
//        .getOrElse {
//            runCatching {
//                ZonedDateTime.parse(publishedAt)
//                    .withZoneSameInstant(ZoneId.systemDefault())
//            }.getOrDefault(ZonedDateTime.now())
//        }
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