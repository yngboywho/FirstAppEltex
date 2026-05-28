package com.eltex.firstapp.feature.event.domain

interface EventsRepository {
    fun getEvents(callback: Callback<List<Event>>)
    fun save(content: String, author: String, status: String = "", visit: String = "",
             link: String = "", callback: Callback<Event>)
    fun update(id: Long, content: String, callback: Callback<Event>)
    fun likeById(id: Long, likedByMe: Boolean, callback: Callback<Event>)
    fun participateById(id: Long, participatedByMe: Boolean, callback: Callback<Event>)
    fun deleteById(id: Long, callback: Callback<Unit>)
}