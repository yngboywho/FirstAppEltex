package com.eltex.firstapp.feature.event.data

import com.eltex.firstapp.feature.data.HttpClientFactory
import com.eltex.firstapp.feature.event.data.EventApi.deleteEvent
import com.eltex.firstapp.feature.event.data.EventApi.getAllEvents
import com.eltex.firstapp.feature.event.data.EventApi.like
import com.eltex.firstapp.feature.event.data.EventApi.participate
import com.eltex.firstapp.feature.event.data.EventApi.saveEvent
import com.eltex.firstapp.feature.event.data.EventApi.unlike
import com.eltex.firstapp.feature.event.data.EventApi.unparticipate
import com.eltex.firstapp.feature.event.data.EventApi.updateEvent
import com.eltex.firstapp.feature.event.domain.Event
import com.eltex.firstapp.feature.event.domain.EventsRepository
import io.ktor.client.HttpClient

class EventsRepositoryImpl(
    private val client: HttpClient = HttpClientFactory.client,
) : EventsRepository {

    override suspend fun getEvents(): List<Event> = client.getAllEvents()
        .map { it.toEvent() }

    override suspend fun save(content: String): Event =
        client.saveEvent(EventDto(content = content)).toEvent()


    override suspend fun update(
        id: Long,
        content: String,
    ): Event =
        client.updateEvent(id, EventDto(content = content)).toEvent()


    override suspend fun likeById(
        id: Long,
        likedByMe: Boolean,
    ): Event = if (likedByMe) {
        client.unlike(id)
    } else {
        client.like(id)
    }.toEvent()

    override suspend fun participateById(
        id: Long,
        participatedByMe: Boolean,
    ): Event = if (participatedByMe) {
        client.unparticipate(id)
    } else {
        client.participate(id)
    }.toEvent()

    override suspend fun deleteById(id: Long) {
        client.deleteEvent(id)
    }
}