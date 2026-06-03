package com.eltex.firstapp.feature.event.data

import com.eltex.firstapp.feature.event.domain.Event
import com.eltex.firstapp.feature.event.domain.EventsRepository

class EventsRepositoryImpl : EventsRepository {

    override suspend fun getEvents(): List<Event> = EventApi.value.getEvents()
        .map { it.toEvent() }

    override suspend fun save(content: String): Event =
        EventApi.value.saveEvent(EventDto(content = content)).toEvent()


    override suspend fun update(
        id: Long,
        content: String,
    ): Event =
        EventApi.value.updateEvent(id, EventDto(content = content)).toEvent()


    override suspend fun likeById(
        id: Long,
        likedByMe: Boolean,
    ): Event = if (likedByMe) {
        EventApi.value.unlike(id)
    } else {
        EventApi.value.like(id)
    }.toEvent()

    override suspend fun participateById(
        id: Long,
        participatedByMe: Boolean,
    ): Event = if (participatedByMe) {
        EventApi.value.unparticipate(id)
    } else {
        EventApi.value.participate(id)
    }.toEvent()

    override suspend fun deleteById(id: Long) = EventApi.value.delete(id)

}