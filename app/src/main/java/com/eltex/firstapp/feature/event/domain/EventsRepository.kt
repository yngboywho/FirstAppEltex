package com.eltex.firstapp.feature.event.domain

interface EventsRepository {
    suspend fun getEvents(): List<Event> = emptyList()
    suspend fun save(content: String): Event
    suspend fun update(id: Long, content: String): Event
    suspend fun likeById(id: Long, likedByMe: Boolean): Event = throw RuntimeException("Not implemented")
    suspend fun participateById(id: Long, participatedByMe: Boolean): Event = throw RuntimeException("Not implemented")
    suspend fun deleteById(id: Long) = Unit
}