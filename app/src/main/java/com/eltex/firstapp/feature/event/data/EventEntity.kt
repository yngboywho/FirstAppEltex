package com.eltex.firstapp.feature.event.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eltex.firstapp.feature.event.domain.Event

@Entity("Events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long = 0,
    @ColumnInfo
    val publishedAt: String,
    @ColumnInfo
    val published: String = "",
    @ColumnInfo
    val status: String = "",
    @ColumnInfo
    val visit: String = "",
    @ColumnInfo
    val content: String = "",
    @ColumnInfo
    val author: String = "",
    @ColumnInfo
    val link: String = "",
    @ColumnInfo
    val likes: Int = 0,
    @ColumnInfo
    val likedByMe: Boolean = false,
    @ColumnInfo
    val participants: Int = 0,
    @ColumnInfo
    val participantsByMe: Boolean = false,
) {
    fun toDomain() = Event(
        id = id,
        publishedAt = publishedAt,
        published = published,
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