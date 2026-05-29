package com.eltex.firstapp.feature.event.data

import com.eltex.firstapp.feature.event.domain.Event
import com.eltex.firstapp.feature.event.domain.EventsRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class EventsRepositoryImpl : EventsRepository {


    override fun getEvents(): Single<List<Event>> = EventApi.value.getEvents()
        .map { eventDtos ->
            eventDtos.map { it.toDomain() }
        }

    override fun save(
        content: String,
        author: String,
        status: String,
        visit: String,
        link: String,
    ): Single<Event> {
        val request = EventRequest(
            content = content,
            type = status,
            datetime = visit,
            link = link.ifBlank { null },
        )
        return EventApi.value.saveEvent(request)
            .map { it.toDomain() }
    }

    override fun update(
        id: Long,
        content: String,
    ): Single<Event> = getEvents()
        .flatMap { events ->
            val existing = events.first { it.id == id }
            val request = EventRequest(
                content = content,
                type = existing.status,
                datetime = existing.visit,
                link = existing.link.ifBlank { null },
            )
            EventApi.value.updateEvent(id, request)
        }.map { it.toDomain() }

    override fun likeById(
        id: Long,
        likedByMe: Boolean,
    ): Single<Event> = if (likedByMe) {
        EventApi.value.unlike(id)
    } else {
        EventApi.value.like(id)
    }.map { it.toDomain() }

    override fun participateById(
        id: Long,
        participatedByMe: Boolean,
    ): Single<Event> = if (participatedByMe) {
        EventApi.value.unparticipate(id)
    } else {
        EventApi.value.participate(id)
    }.map { it.toDomain() }

    override fun deleteById(id: Long): Completable = EventApi.value.delete(id)

}