package com.eltex.firstapp.feature.event.data

import com.eltex.firstapp.feature.event.domain.Event
import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
    val id: Long = 0,
    val authorId: Long = 0,
    val author: String = "",
    val authorAvatar: String? = null,
    val content: String = "",
    val datetime: String = "",
    val published: String = "",
    val type: String = "ONLINE",
    val likeOwnerIds: List<Long> = emptyList(),
    val likedByMe: Boolean = false,
    val speakerIds: List<Long> = emptyList(),
    val participantsIds: List<Long> = emptyList(),
    val participatedByMe: Boolean = false,
    val link: String? = null,
) {
    fun toDomain() = Event(
        id = id,
        publishedAt = datetime,
        published = published,
        status = type,
        visit = datetime,
        content = content,
        author = author,
        link = link.orEmpty(),
        likes = likeOwnerIds.size,
        likedByMe = likedByMe,
        participants = participantsIds.size,
        participantsByMe = participatedByMe,
    )
}