package com.eltex.firstapp.feature.event.domain

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface EventsRepository {
    fun getEvents(): Single<List<Event>>
    fun save(content: String, author: String, status: String = "", visit: String = "",
             link: String = ""): Single<Event>
    fun update(id: Long, content: String): Single<Event>
    fun likeById(id: Long, likedByMe: Boolean): Single<Event>
    fun participateById(id: Long, participatedByMe: Boolean): Single<Event>
    fun deleteById(id: Long): Completable
}