package com.eltex.firstapp.feature.event.domain

interface EventsRepository {
    fun getAll(): List<Event>
    fun save(content: String, author: String, status: String = "", visit: String = "",
             link: String = ""): Event
    fun update(id: Long, content: String): Event
    fun likeById(id: Long): Event
    fun participateById(id: Long): Event
    fun deleteById(id: Long)
}